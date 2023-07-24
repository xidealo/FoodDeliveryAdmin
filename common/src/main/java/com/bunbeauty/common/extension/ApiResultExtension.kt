package com.bunbeauty.common.extension

import com.bunbeauty.common.ApiError
import com.bunbeauty.common.ApiResult

suspend fun <T, R> ApiResult<T>.getNullableResult(
    onError: (suspend (ApiError) -> R?)? = null,
    onSuccess: (suspend (T) -> R?)
): R? = when (this) {
    is ApiResult.Success -> {
        data?.let {
            onSuccess(data)
        } ?: onError?.invoke(ApiError.DATA_IS_NULL)
    }
    is ApiResult.Error -> {
        onError?.invoke(apiError)
    }
}