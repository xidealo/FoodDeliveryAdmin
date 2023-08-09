package com.bunbeauty.presentation.view_model.menu.edit_menu_product

import com.bunbeauty.domain.model.menu_product.MenuProduct

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
    val hasOldPriceError: Boolean = false,
    val nutrition: String = "",
    val hasNutritionError: Boolean = false,
    val utils: String = "",
    val eventList: List<EditMenuProductEvent> = emptyList(),
    val isLoadingButton: Boolean = false
) {

    enum class State {
        LOADING,
        SUCCESS,
        ERROR,
    }

    operator fun plus(event: EditMenuProductEvent) = copy(eventList = eventList + event)
    operator fun minus(events: List<EditMenuProductEvent>) =
        copy(eventList = eventList - events.toSet())
}