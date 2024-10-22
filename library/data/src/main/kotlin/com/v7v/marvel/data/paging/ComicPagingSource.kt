package com.v7v.marvel.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.v7v.marvel.data.remote.MarvelComicsService
import com.v7v.marvel.data.remote.models.FetchComicsQueries
import com.v7v.marvel.data.remote.result.mapSuccessOrNullable
import com.v7v.marvel.data.repositories.toComic
import com.v7v.marvel.domain.models.Comic

internal class ComicPagingSource(
    private val service: MarvelComicsService,
    private val titleStartsWith: String?,
) : PagingSource<Int, Comic>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comic> {
        return try {
            val offset = params.key ?: 0

            val response = service.fetchComics(
                FetchComicsQueries(titleStartsWith, limit = params.loadSize, offset = offset),
            )

            val result = response.mapSuccessOrNullable()
            val characters = result?.results?.map { it.toComic() } ?: emptyList()

            LoadResult.Page(
                data = characters,
                prevKey = if (offset == 0) null else offset - params.loadSize,
                nextKey = if (characters.isEmpty()) null else offset + params.loadSize,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Comic>): Int? = null
}
