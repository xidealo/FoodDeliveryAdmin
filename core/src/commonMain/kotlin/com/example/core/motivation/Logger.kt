package com.example.core.motivation

object Logger {
    private const val COMMON_TAG = "commonTag"

    fun logD(
        tag: String,
        message: Any,
    ) {
        log(LogLevel.DEBUG, tag = COMMON_TAG, message.toString())
        log(LogLevel.DEBUG, tag = tag, message.toString())
    }

    fun logE(
        tag: String,
        message: Any,
    ) {
        log(LogLevel.ERROR, tag = COMMON_TAG, message.toString())
        log(LogLevel.ERROR, tag = tag, message.toString())
    }

    fun logW(
        tag: String,
        message: Any,
    ) {
        log(LogLevel.WARNING, tag = COMMON_TAG, message.toString())
        log(LogLevel.WARNING, tag = tag, message.toString())
    }

    enum class LogLevel {
        DEBUG,
        ERROR,
        WARNING,
    }
}

expect fun log(
    logLevel: Logger.LogLevel,
    tag: String,
    message: String,
)
