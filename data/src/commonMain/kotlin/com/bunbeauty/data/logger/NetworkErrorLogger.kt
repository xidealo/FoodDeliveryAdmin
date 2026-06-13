package com.bunbeauty.data.logger

interface NetworkErrorLogger {
    fun logWarning(
        code: Int,
        message: String,
        throwable: Throwable,
    )
}
