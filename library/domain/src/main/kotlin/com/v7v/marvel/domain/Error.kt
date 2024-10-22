package com.v7v.marvel.domain

import com.ioki.textref.TextRef

sealed class Error(val message: TextRef) {
    data class Known(val errorMessage: TextRef) : Error(errorMessage)
    data object Unknown : Error(TextRef.string("Something went wrong.")) // TODO strings.xml
    data object Connectivity : Error(TextRef.string("Check your internet.")) // TODO from strings.xml

    companion object {
        fun connectivityFailure(): Result.Failure<Connectivity> = Result.Failure(Connectivity)
        fun unknownFailure(): Result.Failure<Unknown> = Result.Failure(Unknown)
        fun knownFailure(message: String): Result.Failure<Known> = Result.Failure(Known(TextRef.string(message)))

        fun knownFailure(message: TextRef): Result.Failure<Known> = Result.Failure(Known(message))
    }
}
