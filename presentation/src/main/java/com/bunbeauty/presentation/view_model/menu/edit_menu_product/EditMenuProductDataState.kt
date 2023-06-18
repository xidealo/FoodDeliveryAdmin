package com.bunbeauty.presentation.view_model.menu.edit_menu_product

data class EditMenuProductDataState(
    val state: State = State.LOADING,
    val eventList: List<EditMenuProductEvent> = emptyList()
){

    enum class State {
        LOADING,
        SUCCESS,
        ERROR,
    }

    operator fun plus(event: EditMenuProductEvent) = copy(eventList = eventList + event)
    operator fun minus(events: List<EditMenuProductEvent>) = copy(eventList = eventList - events.toSet())
}
