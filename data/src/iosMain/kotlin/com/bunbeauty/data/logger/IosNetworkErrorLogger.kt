package com.bunbeauty.data.logger

class IosNetworkErrorLogger : NetworkErrorLogger {
    override fun logWarning(
        code: Int,
        message: String,
        throwable: Throwable,
    ) = Unit
}
