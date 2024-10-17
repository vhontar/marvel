package com.v7v.marvel.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.v7v.marvel.data.local.dao.CharacterDao
import com.v7v.marvel.data.local.dao.ComicDao
import com.v7v.marvel.data.local.entities.CharacterEntity
import com.v7v.marvel.data.local.entities.ComicEntity

@Database(
    entities = [CharacterEntity::class, ComicEntity::class],
    version = 1,
    exportSchema = false,
)
internal abstract class MarvelDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun comicDao(): ComicDao
}