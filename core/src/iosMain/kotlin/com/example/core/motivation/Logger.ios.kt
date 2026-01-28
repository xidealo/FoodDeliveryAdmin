package com.example.core.motivation

actual fun log(
    logLevel: Logger.LogLevel,
    tag: String,
    message: String,
) {
    println("$tag : $message")
}
