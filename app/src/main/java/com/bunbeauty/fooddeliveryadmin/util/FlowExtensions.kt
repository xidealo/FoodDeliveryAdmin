package com.bunbeauty.fooddeliveryadmin.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
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
