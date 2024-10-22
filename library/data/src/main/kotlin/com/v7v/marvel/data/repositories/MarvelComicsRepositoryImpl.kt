package com.v7v.marvel.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.v7v.marvel.data.paging.ComicPagingSource
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

internal class MarvelComicsRepositoryImpl(
    private val service: MarvelComicsService,
) : MarvelComicsRepository {

    override suspend fun getComicsPaged(titleStartsWith: String?): Flow<PagingData<Comic>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = { ComicPagingSource(service, titleStartsWith) },
    ).flow

    override suspend fun getComic(comicId: Int): Result<Comic, Error> = service.fetchComic(comicId).map(
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
    characters = characters.items.map { Comic.Character(it.name) },
)
