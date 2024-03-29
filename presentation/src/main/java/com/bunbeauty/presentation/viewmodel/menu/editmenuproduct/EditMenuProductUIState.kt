package com.bunbeauty.presentation.viewmodel.menu.editmenuproduct

data class EditMenuProductUIState(
    val title: String,
    val editMenuProductState: EditMenuProductState = EditMenuProductState.Loading,
    val eventList: List<EditMenuProductEvent> = emptyList()
) {

    val getSuccessState = (editMenuProductState as? EditMenuProductState.Success)

    sealed interface EditMenuProductState {

        data object Loading : EditMenuProductState
        data class Success(
            val name: String,
            val hasNameError: Boolean,

            val description: String,
            val hasDescriptionError: Boolean,

            val newPrice: String,
            val hasNewPriceError: Boolean,

            val oldPrice: String,

            val nutrition: String,

            val utils: String,

            val isLoadingButton: Boolean,

            val comboDescription: String,

            val isVisible: Boolean,
        ) : EditMenuProductState

        data object Error : EditMenuProductState
    }
}

