package com.v7v.marvel.data.remote.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorBody(
    val code: String,
    val message: String,
)