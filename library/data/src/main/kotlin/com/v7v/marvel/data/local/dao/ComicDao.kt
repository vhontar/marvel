package com.v7v.marvel.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.v7v.marvel.data.local.entities.ComicEntity

@Dao
internal interface ComicDao {
    @Query("SELECT * FROM comics_table WHERE (:query IS NULL OR title LIKE '%' || :query || '%')")
    fun getAllComicsPaged(query: String?): PagingSource<Int, ComicEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(comics: List<ComicEntity>)

    @Update
    suspend fun update(comic: ComicEntity)

    @Query("DELETE FROM comics_table")
    suspend fun deleteAll()

    @Query("SELECT MAX(updatedAt) FROM comics_table")
    suspend fun getLatestUpdatedTimestamp(): Long?
}
