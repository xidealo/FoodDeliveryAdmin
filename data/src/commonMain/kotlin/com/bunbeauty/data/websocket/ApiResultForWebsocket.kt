package com.bunbeauty.data.websocket

import common.ApiError

sealed class ApiResultForWebsocket<out T> {
    data class Loading(
        val isLoading: Boolean,
    ) : ApiResultForWebsocket<Nothing>()

    data class Success<T>(
        val data: T,
    ) : ApiResultForWebsocket<T>()

    data class Error(
        val apiError: ApiError,
    ) : ApiResultForWebsocket<Nothing>()
}
