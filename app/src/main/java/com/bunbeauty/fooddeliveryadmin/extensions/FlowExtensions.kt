package com.bunbeauty.fooddeliveryadmin.extensions

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Flow<T>.launchWhenStarted(lifecycleCoroutineScope: LifecycleCoroutineScope){
    lifecycleCoroutineScope.launchWhenStarted {
        this@launchWhenStarted.collect()
    }
}

fun <T> Flow<T>.startedLaunch(lifecycle: Lifecycle){
    lifecycle.coroutineScope.launch {
        this@startedLaunch
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect()
    }
}
