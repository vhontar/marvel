package com.v7v.marvel.domain.repositories

import androidx.paging.PagingData
import com.v7v.marvel.domain.Error
import com.v7v.marvel.domain.Result
import com.v7v.marvel.domain.models.Comic
import kotlinx.coroutines.flow.Flow

interface MarvelComicsRepository {
    suspend fun getComicsPaged(titleStartsWith: String?): Flow<PagingData<Comic>>
    suspend fun getComic(comicId: Int): Result<Comic, Error>
}
