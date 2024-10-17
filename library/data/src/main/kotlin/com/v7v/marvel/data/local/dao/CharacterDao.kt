package com.v7v.marvel.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.v7v.marvel.data.local.entities.CharacterEntity

@Dao
internal interface CharacterDao {
    @Query("SELECT * FROM characters_table WHERE (:query IS NULL OR name LIKE '%' || :query || '%')")
    fun getAllCharactersPaged(query: String?): PagingSource<Int, CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Update
    suspend fun update(character: CharacterEntity)

    @Query("DELETE FROM characters_table")
    suspend fun deleteAll()

    @Query("SELECT MAX(updatedAt) FROM characters_table")
    suspend fun getLatestUpdatedTimestamp(): Long?
}
