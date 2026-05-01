package com.bunbeauty.shared.designsystem.compose.element

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

@Composable
fun rememberMultipleEventsCutter(eventThreshold: Long = 300L): MultipleEventsCutter =
    remember {
        DefaultMultipleEventsCutter(eventThreshold = eventThreshold)
    }

interface MultipleEventsCutter {
    fun processEvent(event: () -> Unit)
}

private class DefaultMultipleEventsCutter(
    private val eventThreshold: Long,
) : MultipleEventsCutter {
    private var lastEventMark: TimeMark? = null

    override fun processEvent(event: () -> Unit) {
        val last = lastEventMark
        if (last == null || last.elapsedNow() >= eventThreshold.milliseconds) {
            event()
            lastEventMark = TimeSource.Monotonic.markNow()
        }
    }
}
