package com.v7v.marvel.data.remote.internal

import com.v7v.marvel.data.remote.models.FetchCharactersQueries
import com.v7v.marvel.data.remote.models.FetchComicsQueries
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

internal class MarvelApi(private val client: HttpClient) {
    suspend fun fetchCharacters(queries: FetchCharactersQueries): HttpResponse =
        client.get("/v1/public/characters") {
            url {
                if (queries.nameStartsWith.isNullOrEmpty().not()) {
                    parameters.append("nameStartsWith", queries.nameStartsWith!!)
                }
                parameters.append("limit", queries.limit.toString())
                parameters.append("offset", queries.offset.toString())
            }
        }

    suspend fun fetchCharacter(characterId: Int): HttpResponse =
        client.get("/v1/public/characters/$characterId")

    suspend fun fetchCharacterComics(characterId: String): HttpResponse =
        client.get("/v1/public/characters/$characterId/comics")

    suspend fun fetchComics(queries: FetchComicsQueries): HttpResponse =
        client.get("/v1/public/comics") {
            url {
                if (queries.titleStartsWith.isNullOrEmpty().not()) {
                    parameters.append("titleStartsWith", queries.titleStartsWith!!)
                }
                parameters.append("limit", queries.limit.toString())
                parameters.append("offset", queries.offset.toString())
                parameters.append("format", queries.format)
            }
        }

    suspend fun fetchComic(comicId: Int): HttpResponse =
        client.get("/v1/public/comics/$comicId")

    suspend fun fetchComicCharacters(comicId: String): HttpResponse =
        client.get("/v1/public/comics/$comicId/characters")
}