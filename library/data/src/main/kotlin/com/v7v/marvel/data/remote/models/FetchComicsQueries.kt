package com.v7v.marvel.data.remote.models

data class FetchComicsQueries(
    val titleStartsWith: String?,
    val limit: Int,
    val offset: Int,
    val format: String = "comic"
)
