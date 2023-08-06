package com.bunbeauty.presentation.view_model.menu.edit_menu_product

data class EditMenuProductUIState(
    val title: String,
    val editMenuProductState: EditMenuProductState = EditMenuProductState.Loading,
    val eventList: List<EditMenuProductEvent> = emptyList()
) {

    sealed interface EditMenuProductState {

        object Loading : EditMenuProductState
        data class Success(
            val name: String,
            val hasNameError: Boolean,

            val description: String,
            val hasDescriptionError: Boolean,

            val newPrice: String,
            val hasNewPriceError: Boolean,

            val oldPrice: String,
            val hasOldPriceError: Boolean,

            val nutrition: String,
            val hasNutritionError: Boolean,

            val utils: String,
        ) : EditMenuProductState

        object Error : EditMenuProductState
    }
}

