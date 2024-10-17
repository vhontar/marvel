package com.v7v.marvel.domain.models

data class Comic(
    val id: Int,
    val title: String,
    val thumbnailUrl: String,
    val characters: List<Character>,
) {
    data class Character(
        val name: String,
    )
}
