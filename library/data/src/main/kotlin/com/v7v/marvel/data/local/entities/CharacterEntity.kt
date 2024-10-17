package com.v7v.marvel.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters_table")
internal data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val thumbnailUrl: String,
    val updatedAt: Long,
)