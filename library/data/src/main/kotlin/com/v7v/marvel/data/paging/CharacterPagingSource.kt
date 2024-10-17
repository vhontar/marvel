package com.v7v.marvel.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.v7v.marvel.data.remote.MarvelCharactersService
import com.v7v.marvel.data.remote.models.FetchCharactersQueries
import com.v7v.marvel.data.remote.result.mapSuccessOrNullable
import com.v7v.marvel.data.repositories.toCharacter
import com.v7v.marvel.domain.models.Character

internal class CharacterPagingSource(
    private val service: MarvelCharactersService,
    private val nameStartsWith: String?,
) : PagingSource<Int, Character>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val offset = params.key ?: 0

            val response = service.fetchCharacters(
                FetchCharactersQueries(nameStartsWith, limit = params.loadSize, offset = offset)
            )

            val result = response.mapSuccessOrNullable()
            val characters = result?.results?.map { it.toCharacter() } ?: emptyList()

            LoadResult.Page(
                data = characters,
                prevKey = if (offset == 0) null else offset - params.loadSize,
                nextKey = if (characters.isEmpty()) null else offset + params.loadSize
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? = null
}