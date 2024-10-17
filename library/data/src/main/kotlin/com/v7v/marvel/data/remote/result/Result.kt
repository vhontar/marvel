package com.v7v.marvel.data.remote.result

sealed class Result<out T> {
    abstract fun <R> map(mapper: (T) -> R): Result<R>

    data class Success<out T>(val data: T) : Result<T>() {
        override fun <R> map(mapper: (T) -> R): Success<R> = Success(mapper(data))
    }

    sealed class Error : Result<Nothing>() {
        override fun <R> map(mapper: (Nothing) -> R): Error = this

        data class Api(
            val statusCode: Int,
            val code: String,
            val message: String,
        ) : Error()

        data class Connectivity(val error: Throwable) : Error()

        data class Generic(val error: Throwable) : Error()
    }
}

fun <T : Any, R : Any> Result<T>.map(
    onSuccess: (T) -> R,
    onApiError: (Result.Error.Api) -> R,
    onConnectivityError: (Result.Error.Connectivity) -> R,
    onGenericError: (Result.Error.Generic) -> R,
): R = when (this) {
    is Result.Success -> onSuccess(this.data)
    is Result.Error.Api -> onApiError(this)
    is Result.Error.Connectivity -> onConnectivityError(this)
    is Result.Error.Generic -> onGenericError(this)
}

fun <T : Any> Result<T>.mapSuccessOrNullable(): T? = when (this) {
    is Result.Success -> data
    is Result.Error.Api -> null
    is Result.Error.Connectivity -> null
    is Result.Error.Generic -> null
}