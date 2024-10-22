package com.v7v.marvel.data.remote.internal.utils

import kotlinx.serialization.json.Json

internal fun createJson() = Json {
    coerceInputValues = true
    ignoreUnknownKeys = true
    explicitNulls = false
}
