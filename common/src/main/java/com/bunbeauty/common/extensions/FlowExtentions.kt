package com.bunbeauty.common.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.flow

fun <T> Flow<T>.launchWhenStarted(lifecycleCoroutineScope: LifecycleCoroutineScope){
    lifecycleCoroutineScope.launchWhenStarted {
        this@launchWhenStarted.collect()
    }
}

fun <T, K> Flow<T>.groupToList(getKey: (T) -> K): Flow<Pair<K, List<T>>> = flow {
    val storage = mutableMapOf<K, MutableList<T>>()
    collect { t -> storage.getOrPut(getKey(t)) { mutableListOf() } += t }
    storage.forEach { (k, ts) -> emit(k to ts) }
}