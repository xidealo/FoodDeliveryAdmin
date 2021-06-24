package com.bunbeauty.fooddeliveryadmin.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import androidx.lifecycle.LifecycleCoroutineScope

fun <T> Flow<T>.launchWhenStarted(lifecycleCoroutineScope: LifecycleCoroutineScope){
    lifecycleCoroutineScope.launchWhenStarted {
        this@launchWhenStarted.collect()
    }
}