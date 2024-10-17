package com.v7v.marvel.data.remote.models

data class FetchCharactersQueries(
    val nameStartsWith: String?,
    val limit: Int,
    val offset: Int,
)
