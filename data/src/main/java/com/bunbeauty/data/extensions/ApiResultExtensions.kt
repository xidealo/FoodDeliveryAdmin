package com.bunbeauty.data.extensions

import com.bunbeauty.common.ApiResult

fun <T> ApiResult<T>.dataOrNull(): T? = if (this is ApiResult.Success) data else null
