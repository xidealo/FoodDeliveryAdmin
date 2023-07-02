package com.bunbeauty.fooddeliveryadmin.compose.element

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberMultipleEventsCutter(
    eventThreshold: Long = 300L
): MultipleEventsCutter = remember {
    DefaultMultipleEventsCutter(eventThreshold = eventThreshold)
}

interface MultipleEventsCutter {
    fun processEvent(event: () -> Unit)
}

private class DefaultMultipleEventsCutter(
    private val eventThreshold: Long
) : MultipleEventsCutter {

    private val currentTime: Long
        get() = System.currentTimeMillis()

    private var lastEventTime: Long = 0

    override fun processEvent(event: () -> Unit) {
        if (currentTime - lastEventTime >= eventThreshold) {
            event()
        }
        lastEventTime = currentTime
    }
}