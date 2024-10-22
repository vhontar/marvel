package com.v7v.marvel.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comics_table")
data class ComicEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val thumbnailUrl: String,
    val updatedAt: Long,
)
