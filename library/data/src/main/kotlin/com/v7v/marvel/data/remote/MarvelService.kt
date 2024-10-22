package com.v7v.marvel.data.remote

import com.v7v.marvel.data.remote.internal.Logging
import com.v7v.marvel.data.remote.internal.MarvelApi
import com.v7v.marvel.data.remote.internal.MarvelHttpClient
import com.v7v.marvel.data.remote.models.ApiBody
import com.v7v.marvel.data.remote.models.ApiCharacterResponse
import com.v7v.marvel.data.remote.models.ApiComicResponse
import com.v7v.marvel.data.remote.models.ApiErrorBody
import com.v7v.marvel.data.remote.models.FetchCharactersQueries
import com.v7v.marvel.data.remote.models.FetchComicsQueries
import com.v7v.marvel.data.remote.result.Result
import com.v7v.marvel.logger.logError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.channels.ClosedChannelException
import kotlin.coroutines.cancellation.CancellationException

fun MarvelService(
    baseUrl: String,
    publicToken: String,
    privateToken: String,
    logging: Logging? = null,
): MarvelService {
    val httpClient = MarvelHttpClient(baseUrl, publicToken, privateToken, logging)
    val marvelApi = MarvelApi(httpClient)
    return DefaultMarvelService(marvelApi)
}

interface MarvelService : MarvelCharactersService, MarvelComicsService

interface MarvelCharactersService {
    suspend fun fetchCharacters(queries: FetchCharactersQueries): Result<ApiBody.ApiData<ApiCharacterResponse>>

    suspend fun fetchCharacter(characterId: Int): Result<ApiBody.ApiData<ApiCharacterResponse>>
}

interface MarvelComicsService {
    suspend fun fetchComics(queries: FetchComicsQueries): Result<ApiBody.ApiData<ApiComicResponse>>

    suspend fun fetchComic(comicId: Int): Result<ApiBody.ApiData<ApiComicResponse>>
}

internal class DefaultMarvelService(
    private val marvelApi: MarvelApi,
) : MarvelService {

    override suspend fun fetchCharacters(
        queries: FetchCharactersQueries,
    ): Result<ApiBody.ApiData<ApiCharacterResponse>> =
        apiCall<ApiBody<ApiCharacterResponse>, ApiBody.ApiData<ApiCharacterResponse>> {
            fetchCharacters(queries)
        }

    override suspend fun fetchCharacter(characterId: Int): Result<ApiBody.ApiData<ApiCharacterResponse>> =
        apiCall<ApiBody<ApiCharacterResponse>, ApiBody.ApiData<ApiCharacterResponse>> {
            fetchCharacter(characterId)
        }

    override suspend fun fetchComics(queries: FetchComicsQueries): Result<ApiBody.ApiData<ApiComicResponse>> =
        apiCall<ApiBody<ApiComicResponse>, ApiBody.ApiData<ApiComicResponse>> {
            fetchComics(queries)
        }

    override suspend fun fetchComic(comicId: Int): Result<ApiBody.ApiData<ApiComicResponse>> =
        apiCall<ApiBody<ApiComicResponse>, ApiBody.ApiData<ApiComicResponse>> {
            fetchComic(comicId)
        }

    private suspend inline fun <reified R, reified T> apiCall(
        crossinline block: suspend MarvelApi.() -> HttpResponse,
    ): Result<T> {
        return try {
            val response = marvelApi.block()
            if (response.status.isSuccess()) {
                mapSuccess<R, T>(response)
            } else {
                mapApiError(response)
            }
        } catch (e: Exception) {
            logError(e) { "Failed to parse data." }
            when {
                e is CancellationException -> throw e
                e.isConnectivityError -> Result.Error.Connectivity(e)
                else -> Result.Error.Generic(e)
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
internal suspend inline fun <reified R, reified T> mapSuccess(successfulResponse: HttpResponse): Result.Success<T> {
    val body = successfulResponse.body<R>()
    val data = when (body) {
        null -> Unit as? T // For Void (no content) body
        is T -> body
        is ApiBody<*> ->
            when {
                body.data is T -> body.data
                T::class == String::class -> body.data.toString()
                else -> null
            }

        else -> null
    } ?: throw IllegalArgumentException("Failed to convert body '$body' to type ${T::class}")
    return Result.Success(data) as Result.Success<T>
}

internal suspend fun mapApiError(failedResponse: HttpResponse): Result.Error.Api {
    val apiErrorBody = failedResponse.body<ApiErrorBody?>()
    val code = failedResponse.status.value
    return Result.Error.Api(
        statusCode = code,
        code = apiErrorBody?.code ?: "",
        message = apiErrorBody?.message ?: "",
    )
}

internal val Throwable.isConnectivityError: Boolean
    get() =
        this is SocketException ||
            this is SocketTimeoutException ||
            this is UnknownHostException ||
            this is ClosedChannelException ||
            this is ConnectException
