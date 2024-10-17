package com.v7v.marvel.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.v7v.marvel.data.local.dao.ComicDao
import com.v7v.marvel.data.local.entities.ComicEntity
import com.v7v.marvel.data.remote.MarvelComicsService
import com.v7v.marvel.data.remote.models.ApiComicResponse
import com.v7v.marvel.data.remote.models.FetchComicsQueries
import com.v7v.marvel.data.remote.result.mapSuccessOrNullable
import java.io.IOException
import java.util.concurrent.TimeUnit

private const val CACHE_EXPIRATION_MINUTES = 10L

@OptIn(ExperimentalPagingApi::class)
internal class ComicRemoteMediator(
    private val service: MarvelComicsService,
    private val dao: ComicDao,
    private val titleStartsWith: String?,
) : RemoteMediator<Int, ComicEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ComicEntity>,
    ): MediatorResult {
        return try {
            val isCacheExpired = isCacheExpired()

            if (loadType == LoadType.REFRESH && !isCacheExpired) {
                return MediatorResult.Success(endOfPaginationReached = false)
            }

            val limit = state.config.pageSize
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = true)
                    state.pages.sumOf { it.data.size }
                }
            }

            val response = service.fetchComics(
                queries = FetchComicsQueries(
                    titleStartsWith = titleStartsWith,
                    limit = limit,
                    offset = offset
                )
            )

            if (loadType == LoadType.REFRESH) {
                dao.deleteAll()
            }

            val result = response.mapSuccessOrNullable()
            val entities = result?.results?.map { it.toComicEntity() } ?: emptyList()
            dao.insertAll(entities)

            MediatorResult.Success(endOfPaginationReached = entities.isEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun isCacheExpired(): Boolean {
        val latestTimestamp = dao.getLatestUpdatedTimestamp()
        val currentTime = System.currentTimeMillis()

        return latestTimestamp == null || TimeUnit.MILLISECONDS.toMinutes(currentTime - latestTimestamp) > CACHE_EXPIRATION_MINUTES
    }
}

private fun ApiComicResponse.toComicEntity() = ComicEntity(
    id = id,
    title = title,
    thumbnailUrl = "${thumbnail.path}.${thumbnail.extension}",
    updatedAt = System.currentTimeMillis(),
)