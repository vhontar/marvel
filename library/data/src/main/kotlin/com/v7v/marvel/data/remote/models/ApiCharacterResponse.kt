package com.v7v.marvel.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiCharacterResponse(
    val id: Int,
    val name: String,
    val thumbnail: Thumbnail,
    val comics: Comics,
) {
    @Serializable
    data class Thumbnail(
        val path: String,
        val extension: String,
    )

    @Serializable
    data class Comics(
        val items: List<Comic>,
    ) {
        @Serializable
        data class Comic(
            val name: String,
        )
    }
}
