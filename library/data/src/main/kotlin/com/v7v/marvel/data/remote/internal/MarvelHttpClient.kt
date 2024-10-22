package com.v7v.marvel.data.remote.internal

import com.v7v.marvel.data.remote.internal.plugins.HttpLoggingPlugin
import com.v7v.marvel.data.remote.internal.utils.createJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.headers
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import java.security.MessageDigest

private const val TIMEOUT_MILLISECONDS = 20 * 1000L

internal fun httpClient(): HttpClient = HttpClient(OkHttp)

@Suppress("FunctionName")
@OptIn(ExperimentalStdlibApi::class)
internal fun MarvelHttpClient(
    baseUrl: String,
    publicToken: String,
    privateToken: String,
    logging: Logging?,
): HttpClient = httpClient().config {
    install(ContentNegotiation) {
        json(createJson())
    }
    install(HttpTimeout) {
        requestTimeoutMillis = TIMEOUT_MILLISECONDS
        connectTimeoutMillis = TIMEOUT_MILLISECONDS
        socketTimeoutMillis = TIMEOUT_MILLISECONDS
    }
    install(HttpCache)

    if (logging != null) {
        install(HttpLoggingPlugin) {
            this.logging = logging
        }
    }

    defaultRequest {
        headers {
            append("Accept", "application/json")
            append("Content-Type", "application/json")
        }

        val ts = System.currentTimeMillis().toString()
        val md5 = MessageDigest.getInstance("MD5")
        val hash = md5.digest((ts + privateToken + publicToken).toByteArray()).toHexString()

        url(baseUrl)

        url {
            parameters.append("ts", ts)
            parameters.append("apikey", publicToken)
            parameters.append("hash", hash)
        }
    }
}
