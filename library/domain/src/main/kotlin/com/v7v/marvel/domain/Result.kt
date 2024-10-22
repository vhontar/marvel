package com.v7v.marvel.domain

sealed interface Result<out T, out E> {
    data class Success<T>(val data: T) : Result<T, Nothing>
    data class Failure<E>(val error: E) : Result<Nothing, E>
}

fun <T> T.toSuccess() = Result.Success(data = this)
fun <E> E.toFailure() = Result.Failure(error = this)
