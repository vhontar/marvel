package com.v7v.marvel.feature.comic.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ioki.textref.TextRef
import com.v7v.marvel.domain.Result
import com.v7v.marvel.domain.models.Comic
import com.v7v.marvel.domain.repositories.MarvelComicsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

abstract class ComicDetailsViewModel : ViewModel() {
    abstract val state: StateFlow<State>
    abstract fun load(comicId: Int)
}

val comicDetailsViewModelModule = module {
    viewModel<ComicDetailsViewModel> { DefaultComicDetailsViewModel(get()) }
}

internal class DefaultComicDetailsViewModel(
    private val repository: MarvelComicsRepository,
) : ComicDetailsViewModel() {

    private val mutableState = MutableStateFlow<State>(State.Loading)
    override val state: StateFlow<State> = mutableState

    override fun load(comicId: Int) {
        viewModelScope.launch {
            when (val result = repository.getComic(comicId)) {
                is Result.Failure -> mutableState.emit(State.Error(result.error.message))
                is Result.Success -> mutableState.emit(State.Success(result.data))
            }
        }
    }
}

sealed interface State {
    data object Loading : State
    data class Error(val message: TextRef) : State
    data class Success(val comic: Comic) : State
}
