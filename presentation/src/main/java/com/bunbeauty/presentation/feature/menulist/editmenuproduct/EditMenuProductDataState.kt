package com.bunbeauty.presentation.feature.menulist.editmenuproduct

import com.bunbeauty.domain.model.menuproduct.MenuProduct

@Deprecated("")
data class EditMenuProductDataState(
    val state: State = State.LOADING,
    val menuProduct: MenuProduct? = null,
    val constName: String = "",
    val name: String = "",
    val hasNameError: Boolean = false,
    val description: String = "",
    val hasDescriptionError: Boolean = false,
    val newPrice: String = "",
    val hasNewPriceError: Boolean = false,
    val oldPrice: String = "",
    val nutrition: String = "",
    val utils: String = "",
    val comboDescription: String = "",
    val eventList: List<EditMenuProductEvent> = emptyList(),
    val isLoadingButton: Boolean = false,
    val isVisible: Boolean = false
) {

    enum class State {
        LOADING,
        SUCCESS,
        ERROR
    }

    operator fun plus(event: EditMenuProductEvent) = copy(eventList = eventList + event)
    operator fun minus(events: List<EditMenuProductEvent>) =
        copy(eventList = eventList - events.toSet())
}
