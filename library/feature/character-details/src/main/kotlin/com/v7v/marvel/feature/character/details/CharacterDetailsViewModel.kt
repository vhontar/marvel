package com.v7v.marvel.feature.character.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ioki.textref.TextRef
import com.v7v.marvel.domain.Result
import com.v7v.marvel.domain.models.Character
import com.v7v.marvel.domain.repositories.MarvelCharactersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

abstract class CharacterDetailsViewModel : ViewModel() {
    abstract val state: StateFlow<State>
    abstract fun load(characterId: Int)
}

val characterDetailsViewModelModule = module {
    viewModel<CharacterDetailsViewModel> { DefaultCharacterDetailsViewModel(get()) }
}

internal class DefaultCharacterDetailsViewModel(
    private val repository: MarvelCharactersRepository,
) : CharacterDetailsViewModel() {

    private val mutableState = MutableStateFlow<State>(State.Loading)
    override val state: StateFlow<State> = mutableState

    override fun load(characterId: Int) {
        viewModelScope.launch {
            when (val result = repository.getCharacter(characterId)) {
                is Result.Failure -> mutableState.emit(State.Error(result.error.message))
                is Result.Success -> mutableState.emit(State.Success(result.data))
            }
        }
    }
}

sealed interface State {
    data object Loading : State
    data class Error(val message: TextRef) : State
    data class Success(val character: Character) : State
}

