package com.bunbeauty.presentation.viewmodel.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseStateViewModel<DS : BaseDataState, A : BaseAction, E : BaseEvent>(
    initState: DS
) : ViewModel() {

    protected val mutableDataState = MutableStateFlow(initState)
    val state = mutableDataState.asStateFlow()

    protected val mutableEvents = MutableStateFlow<List<E>>(emptyList())
    val events = mutableEvents.asStateFlow()

    fun onAction(action: A) {
        reduce(action, mutableDataState.value)
    }

    protected abstract fun reduce(action: A, dataState: DS)

    fun consumeEvents(events: List<E>) {
        mutableEvents.update { list ->
            list - events.toSet()
        }
    }

    protected inline fun setState(block: DS.() -> DS) {
        mutableDataState.update(block)
    }

    protected inline fun sendEvent(block: (DS) -> E) {
        mutableEvents.update { list ->
            list + block(mutableDataState.value)
        }
    }
}
