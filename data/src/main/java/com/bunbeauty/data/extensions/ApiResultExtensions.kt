package com.bunbeauty.data.extensions

import com.bunbeauty.common.ApiResult

fun <T> ApiResult<T>.dataOrNull(): T? {
    return if (this is ApiResult.Success) data else null
}
