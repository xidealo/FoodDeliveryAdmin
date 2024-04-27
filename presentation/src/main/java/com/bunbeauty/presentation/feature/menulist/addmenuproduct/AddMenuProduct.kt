package com.bunbeauty.presentation.feature.menulist.addmenuproduct

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface AddMenuProduct {
    data class ViewDataState(
        val name: String,
        val hasNameError: Boolean,
        val description: String,
        val hasDescriptionError: Boolean,
        val newPrice: String,
        val hasNewPriceError: Boolean,
        val oldPrice: String,
        val nutrition: String,
        val utils: String,
        val comboDescription: String,
        val isLoadingButton: Boolean,
        val isVisible: Boolean,
        val throwable: Throwable?
    ) : BaseViewDataState

    sealed interface Action : BaseAction {
        data object Init : Action
        data object OnBackClick : Action
        data class OnNameTextChanged(val name: String) : Action
        data class OnNewPriceTextChanged(val newPrice: String) : Action
        data class OnOldPriceTextChanged(val oldPrice: String) : Action
        data class OnNutritionTextChanged(val nutrition: String) : Action
        data class OnDescriptionTextChanged(val description: String) : Action
        data class OnComboDescriptionTextChanged(val comboDescription: String) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
    }
}