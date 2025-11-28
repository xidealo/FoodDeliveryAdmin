package com.bunbeauty.data.extensions

import com.bunbeauty.data.model.server.ServerList

fun <T, R> ServerList<T>.map(block: (T) -> R): List<R> {
    return results.map(block)
}
