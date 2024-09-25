package com.bunbeauty.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

inline fun <reified T> List<Flow<T>>.flattenFlow(): Flow<List<T>> {
    return combine(this@flattenFlow) { array ->
        array.toList()
    }
}
