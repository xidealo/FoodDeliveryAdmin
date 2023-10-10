package com.bunbeauty.presentation.viewmodel.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseStateViewModel<DS : DataState, A : Action, E : Event>(
    initState: DS
): ViewModel() {

    protected val mutableState = MutableStateFlow(initState)
    val state = mutableState.asStateFlow()

    protected val mutableEvents = MutableStateFlow<List<E>>(emptyList())
    val events = mutableEvents.asStateFlow()

    abstract fun handleAction(action: A)

    fun consumeEvents(events: List<E>) {
        mutableEvents.update { list ->
            list - events.toSet()
        }
    }

    protected inline fun state(block: (DS) -> DS) {
        mutableState.update(block)
    }

    protected inline fun event(block: (DS) -> E) {
        mutableEvents.update { list ->
            list + block(mutableState.value)
        }
    }

}