package com.v7v.marvel.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiBody<out T>(
    val code: Int,
    val data: ApiData<T>,
) {
    @Serializable
    data class ApiData<out T>(
        val offset: Int,
        val limit: Int,
        val total: Int,
        val count: Int,
        val results: List<T>,
    )
}