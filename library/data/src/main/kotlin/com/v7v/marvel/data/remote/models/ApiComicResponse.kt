package com.v7v.marvel.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiComicResponse(
    val id: Int,
    val title: String,
    val thumbnail: Thumbnail,
) {
    @Serializable
    data class Thumbnail(
        val path: String,
        val extension: String
    )
}
