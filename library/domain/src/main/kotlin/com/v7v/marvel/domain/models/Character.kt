package com.v7v.marvel.domain.models

data class Character(
    val id: Int,
    val name: String,
    val thumbnailUrl: String,
    val comics: List<Comic>,
) {
    data class Comic(
        val name: String,
    )
}