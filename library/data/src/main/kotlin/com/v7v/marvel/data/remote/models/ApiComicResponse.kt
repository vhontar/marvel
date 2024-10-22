package com.v7v.marvel.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiComicResponse(
    val id: Int,
    val title: String,
    val thumbnail: Thumbnail,
    val characters: Characters,
) {
    @Serializable
    data class Thumbnail(
        val path: String,
        val extension: String,
    )

    @Serializable
    data class Characters(
        val items: List<CharacterItem>,
    ) {
        @Serializable
        data class CharacterItem(
            val name: String,
        )
    }
}
