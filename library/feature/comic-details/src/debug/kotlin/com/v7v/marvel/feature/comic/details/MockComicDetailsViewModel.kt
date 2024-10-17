package com.v7v.marvel.feature.comic.details

import com.v7v.marvel.domain.models.Comic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class MockComicDetailsViewModel : ComicDetailsViewModel() {
    override val state: StateFlow<State> = MutableStateFlow(
        State.Success(
            comic = Comic(
                id = 1,
                title = "SpiderMan Story",
                thumbnailUrl = "test.com",
                characters = listOf(
                    Comic.Character(name = "Spider Boy"),
                    Comic.Character(name = "Spider Boy 2"),
                    Comic.Character(name = "Spider Boy 3"),
                    Comic.Character(name = "Spider Boy 4"),
                )
            )
        )
    )

    override fun load(comicId: Int) {
    }
}