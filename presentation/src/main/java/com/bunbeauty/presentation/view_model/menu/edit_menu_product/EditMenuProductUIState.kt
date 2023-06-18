package com.bunbeauty.presentation.view_model.menu.edit_menu_product

data class EditMenuProductUIState(
    val editMenuProductState: EditMenuProductState = EditMenuProductState.Loading,
    val eventList: List<EditMenuProductEvent> = emptyList()
) {

    sealed interface EditMenuProductState {

        object Loading : EditMenuProductState
        data class Success(val t: Int = 1) : EditMenuProductState
        object Error : EditMenuProductState
    }
}

