package com.v7v.marvel.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.v7v.marvel.data.local.dao.CharacterDao
import com.v7v.marvel.data.local.entities.CharacterEntity
import com.v7v.marvel.data.remote.MarvelCharactersService
import com.v7v.marvel.data.remote.models.ApiCharacterResponse
import com.v7v.marvel.data.remote.models.FetchCharactersQueries
import com.v7v.marvel.data.remote.result.mapSuccessOrNullable
import java.io.IOException
import java.util.concurrent.TimeUnit

private const val CACHE_EXPIRATION_MINUTES = 10L

@OptIn(ExperimentalPagingApi::class)
internal class CharacterRemoteMediator(
    private val service: MarvelCharactersService,
    private val dao: CharacterDao,
    private val nameStartsWith: String?,
) : RemoteMediator<Int, CharacterEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>,
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

            val response = service.fetchCharacters(
                queries = FetchCharactersQueries(
                    nameStartsWith = nameStartsWith,
                    limit = limit,
                    offset = offset
                )
            )

            if (loadType == LoadType.REFRESH) {
                dao.deleteAll()
            }

            val result = response.mapSuccessOrNullable()
            val entities = result?.results?.map { it.toCharacterEntity() } ?: emptyList()
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

private fun ApiCharacterResponse.toCharacterEntity() = CharacterEntity(
    id = id,
    name = name,
    thumbnailUrl = "${thumbnail.path}.${thumbnail.extension}",
    updatedAt = System.currentTimeMillis(),
)