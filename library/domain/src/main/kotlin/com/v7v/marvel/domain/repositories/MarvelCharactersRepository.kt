package com.v7v.marvel.domain.repositories

import androidx.paging.PagingData
import com.v7v.marvel.domain.Error
import com.v7v.marvel.domain.Result
import com.v7v.marvel.domain.models.Character
import kotlinx.coroutines.flow.Flow

interface MarvelCharactersRepository {
    suspend fun getCharactersPaged(nameStartsWith: String?): Flow<PagingData<Character>>
    suspend fun getCharacter(characterId: Int): Result<Character, Error>
}