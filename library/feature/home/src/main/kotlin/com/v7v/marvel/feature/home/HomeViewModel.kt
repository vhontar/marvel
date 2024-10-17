package com.v7v.marvel.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.v7v.marvel.domain.models.Character
import com.v7v.marvel.domain.models.Comic
import com.v7v.marvel.domain.repositories.MarvelCharactersRepository
import com.v7v.marvel.domain.repositories.MarvelComicsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

abstract class HomeViewModel : ViewModel() {
    abstract val charactersPagedFlow: Flow<PagingData<Character>>
    abstract val comicsPagedFlow: Flow<PagingData<Comic>>

    abstract fun searchCharactersByName(name: String)
    abstract fun searchComicsByTitle(title: String)
}

val homeViewModelModule = module {
    viewModel<HomeViewModel> { DefaultHomeViewModel(get(), get()) }
}

@OptIn(ExperimentalCoroutinesApi::class)
internal class DefaultHomeViewModel(
    private val marvelCharactersRepository: MarvelCharactersRepository,
    private val marvelComicsRepository: MarvelComicsRepository,
) : HomeViewModel() {

    private val mutableNameStartsWith = MutableStateFlow<String?>(null)
    private val mutableTitleStartsWith = MutableStateFlow<String?>(null)

    override val charactersPagedFlow: Flow<PagingData<Character>> =
        mutableNameStartsWith.flatMapLatest { query ->
            marvelCharactersRepository.getCharactersPaged(query)
        }.cachedIn(viewModelScope)

    override val comicsPagedFlow: Flow<PagingData<Comic>> =
        mutableTitleStartsWith.flatMapLatest { query ->
            marvelComicsRepository.getComicsPaged(query)
        }.cachedIn(viewModelScope)

    override fun searchCharactersByName(name: String) {
        viewModelScope.launch {
            mutableNameStartsWith.emit(name)
        }
    }

    override fun searchComicsByTitle(title: String) {
        viewModelScope.launch {
            mutableTitleStartsWith.emit(title)
        }
    }
}