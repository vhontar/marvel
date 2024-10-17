package com.v7v.marvel.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.v7v.marvel.data.local.dao.ComicDao
import com.v7v.marvel.data.local.entities.ComicEntity
import com.v7v.marvel.data.paging.ComicRemoteMediator
import com.v7v.marvel.data.remote.MarvelComicsService
import com.v7v.marvel.data.remote.models.ApiComicResponse
import com.v7v.marvel.data.remote.result.map
import com.v7v.marvel.domain.Error
import com.v7v.marvel.domain.Result
import com.v7v.marvel.domain.models.Comic
import com.v7v.marvel.domain.repositories.MarvelComicsRepository
import com.v7v.marvel.domain.toSuccess
import com.v7v.marvel.logger.logError
import com.v7v.marvel.logger.logWarn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MarvelComicsRepositoryImpl(
    private val service: MarvelComicsService,
    private val dao: ComicDao,
) : MarvelComicsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getComicsPaged(
        titleStartsWith: String?,
    ): Flow<PagingData<Comic>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
        ),
        remoteMediator = ComicRemoteMediator(service, dao, titleStartsWith),
        pagingSourceFactory = { dao.getAllComicsPaged(titleStartsWith) }
    ).flow.map { pagingData ->
        pagingData.map { it.toComic() }
    }

    override suspend fun getComic(comicId: Int): Result<Comic, Error> =
        service.fetchComic(comicId).map(
            onSuccess = { result ->
                if (result.results.isEmpty()) {
                    logError { "Failed to load a character." }
                    Error.unknownFailure()
                } else {
                    result.results[0].toComic().toSuccess()
                }
            },
            onApiError = {
                logError { "Failed to load a character. ${it.code}: ${it.message}" }
                Error.knownFailure(it.message)
            },
            onConnectivityError = {
                logWarn(it.error) { "Connectivity error. Failed to load a character." }
                Error.connectivityFailure()
            },
            onGenericError = {
                logError(it.error) { "Failed to load a character." }
                Error.unknownFailure()
            },
        )
}

internal fun ApiComicResponse.toComic() = Comic(
    id = id,
    title = title,
    thumbnailUrl = "${thumbnail.path}.${thumbnail.extension}",
)

internal fun ComicEntity.toComic() = Comic(
    id = id,
    title = title,
    thumbnailUrl = thumbnailUrl,
)