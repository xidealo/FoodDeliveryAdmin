package com.bunbeauty.fooddeliveryadmin.extensions

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Flow<T>.startedLaunch(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.lifecycleScope.launch {
        this@startedLaunch
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .collect()
    }
}
