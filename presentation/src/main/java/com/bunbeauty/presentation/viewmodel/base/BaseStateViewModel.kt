package com.bunbeauty.presentation.viewmodel.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseStateViewModel<DS : BaseDataState, A : BaseAction, E : BaseEvent>(
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

    protected inline fun setState(block: (DS) -> DS) {
        mutableState.update(block)
    }

    protected inline fun addEvent(block: (DS) -> E) {
        mutableEvents.update { list ->
            list + block(mutableState.value)
        }
    }

}