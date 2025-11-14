package com.bunbeauty.data.extensions

import com.bunbeauty.data.model.server.ServerList

fun <T, R> ServerList<T>.map(block: (T) -> R): List<R> = results.map(block)
