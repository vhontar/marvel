package com.v7v.marvel.feature.character.details

import com.v7v.marvel.domain.models.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class MockCharacterDetailsViewModel : CharacterDetailsViewModel() {
    override val state: StateFlow<State> = MutableStateFlow(
        State.Success(
            character = Character(
                id = 1,
                name = "SpiderMan",
                thumbnailUrl = "test.com",
                comics = listOf(
                    Character.Comic(name = "Spider Boy"),
                    Character.Comic(name = "Spider Boy 2"),
                    Character.Comic(name = "Spider Boy 3"),
                    Character.Comic(name = "Spider Boy 4"),
                )
            )
        )
    )

    override fun load(characterId: Int) {
    }
}