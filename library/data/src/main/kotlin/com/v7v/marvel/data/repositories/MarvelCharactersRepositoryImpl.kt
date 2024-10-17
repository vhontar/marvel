package com.v7v.marvel.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.v7v.marvel.data.local.dao.CharacterDao
import com.v7v.marvel.data.local.entities.CharacterEntity
import com.v7v.marvel.data.paging.CharacterRemoteMediator
import com.v7v.marvel.data.remote.MarvelCharactersService
import com.v7v.marvel.data.remote.models.ApiCharacterResponse
import com.v7v.marvel.data.remote.result.map
import com.v7v.marvel.domain.Error
import com.v7v.marvel.domain.Result
import com.v7v.marvel.domain.models.Character
import com.v7v.marvel.domain.repositories.MarvelCharactersRepository
import com.v7v.marvel.domain.toSuccess
import com.v7v.marvel.logger.logError
import com.v7v.marvel.logger.logWarn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MarvelCharactersRepositoryImpl(
    private val service: MarvelCharactersService,
    private val dao: CharacterDao,
) : MarvelCharactersRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getCharactersPaged(
        nameStartsWith: String?,
    ): Flow<PagingData<Character>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
        ),
        remoteMediator = CharacterRemoteMediator(service, dao, nameStartsWith),
        pagingSourceFactory = { dao.getAllCharactersPaged(nameStartsWith) }
    ).flow.map { pagingData ->
        pagingData.map { it.toCharacter() }
    }

    override suspend fun getCharacter(characterId: Int): Result<Character, Error> =
        service.fetchCharacter(characterId).map(
            onSuccess = { result ->
                if (result.results.isEmpty()) {
                    logError { "Failed to load a character." }
                    Error.unknownFailure()
                } else {
                    result.results[0].toCharacter().toSuccess()
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

internal fun ApiCharacterResponse.toCharacter() = Character(
    id = id,
    name = name,
    thumbnailUrl = "${thumbnail.path}.${thumbnail.extension}",
    comics = comics.items.map { Character.Comic(it.name) },
)

internal fun CharacterEntity.toCharacter() = Character(
    id = id,
    name = name,
    thumbnailUrl = thumbnailUrl,
    comics = listOf(), // TODO new model for character details page
)