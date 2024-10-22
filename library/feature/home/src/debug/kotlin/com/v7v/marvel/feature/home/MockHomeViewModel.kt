package com.v7v.marvel.feature.home

import androidx.paging.PagingData
import com.v7v.marvel.domain.models.Character
import com.v7v.marvel.domain.models.Comic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class MockHomeViewModel : HomeViewModel() {
    override val charactersPagedFlow: Flow<PagingData<Character>> = flowOf(
        PagingData.from(
            listOf(
                Character(
                    id = 1,
                    name = "Mock Character 1",
                    thumbnailUrl = "https://example.com/mock1.jpg",
                    comics = listOf(),
                ),
                Character(
                    id = 2,
                    name = "Mock Character 2",
                    thumbnailUrl = "https://example.com/mock2.jpg",
                    comics = listOf(),
                ),
                Character(
                    id = 3,
                    name = "Mock Character 3",
                    thumbnailUrl = "https://example.com/mock3.jpg",
                    comics = listOf(),
                ),
            ),
        ),
    )

    override val comicsPagedFlow: Flow<PagingData<Comic>> = flowOf(
        PagingData.from(
            listOf(
                Comic(
                    id = 1,
                    title = "Mock Comic 1",
                    thumbnailUrl = "https://example.com/mock1.jpg",
                    characters = listOf(),
                ),
                Comic(
                    id = 2,
                    title = "Mock Comic 2",
                    thumbnailUrl = "https://example.com/mock2.jpg",
                    characters = listOf(),
                ),
                Comic(
                    id = 3,
                    title = "Mock Comic 3",
                    thumbnailUrl = "https://example.com/mock3.jpg",
                    characters = listOf(),
                ),
            ),
        ),
    )

    override val currentTabIndex: Flow<Int> = flowOf(0)

    override fun searchComicsByTitle(title: String) {
    }

    override fun searchCharactersByName(name: String) {
    }

    override fun changeCurrentTabIndex(idx: Int) {
    }
}
