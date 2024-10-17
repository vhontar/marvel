package com.v7v.marvel.logger

enum class Severity { DEBUG, INFO, WARN, ERROR }

private typealias MessageBlock = () -> String

inline fun <T : Any> T.logDebug(messageBlock: MessageBlock) {
    if (Logger.isLoggable(Severity.DEBUG)) {
        Logger.logDebug(this, messageBlock())
    }
}

inline fun <T: Any> T.logInfo(messageBlock: MessageBlock) {
    if (Logger.isLoggable(Severity.INFO)) {
        Logger.logInfo(this, messageBlock())
    }
}

inline fun <T: Any> T.logWarn(messageBlock: MessageBlock) {
    if (Logger.isLoggable(Severity.WARN)) {
        Logger.logWarn(this, messageBlock(), null)
    }
}

inline fun <T: Any> T.logWarn(throwable: Throwable, messageBlock: MessageBlock) {
    if (Logger.isLoggable(Severity.WARN)) {
        Logger.logWarn(this, messageBlock(), throwable)
    }
}

inline fun <T: Any> T.logError(messageBlock: MessageBlock) {
    if (Logger.isLoggable(Severity.ERROR)) {
        Logger.logError(this, messageBlock(), null)
    }
}

inline fun <T: Any> T.logError(throwable: Throwable, messageBlock: MessageBlock) {
    if (Logger.isLoggable(Severity.ERROR)) {
        Logger.logError(this, messageBlock(), throwable)
    }
}


data object Logger {

    private var destination: Destination? = null

    fun sendTo(destination: Destination) {
        this.destination?.onDetach()
        this.destination = destination
        this.destination?.onAttach()
    }

    @PublishedApi
    internal fun isLoggable(severity: Severity) = destination?.isLoggable(severity) ?: false

    @PublishedApi
    internal fun logInfo(tag: Any, message: String) = destination?.logInfo(tag.name, message)

    @PublishedApi
    internal fun logDebug(tag: Any, message: String) = destination?.logDebug(tag.name, message)

    @PublishedApi
    internal fun logWarn(tag: Any, message: String?, throwable: Throwable?) {
        if (throwable == null && message.isNullOrEmpty()) return
        destination?.logWarn(tag.name, message, throwable)
    }

    @PublishedApi
    internal fun logError(tag: Any, message: String?, throwable: Throwable?) {
        if (throwable == null && message.isNullOrEmpty()) return
        destination?.logError(tag.name, message, throwable)
    }

    interface Destination {
        fun isLoggable(severity: Severity): Boolean
        fun logInfo(tag: String, message: String)
        fun logDebug(tag: String, message: String)
        fun logWarn(tag: String, message: String?, throwable: Throwable?)
        fun logError(tag: String, message: String?, throwable: Throwable?)
        fun onAttach() = Unit
        fun onDetach() = Unit
    }
}

private val Any.name
    get() = if (this is String) {
        this
    } else {
        val name = this.javaClass.name
        when {
            name.contains("Kt&") -> name.substringBefore("Kt$")
            name.contains("$") -> name.substringBefore("$")
            else -> name
        }
    }