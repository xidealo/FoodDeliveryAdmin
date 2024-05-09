package com.bunbeauty.presentation.feature.menulist.addmenuproduct

import com.bunbeauty.domain.model.Category
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
        val hasOldPriceError: Boolean,
        val nutrition: String,
        val utils: String,
        val comboDescription: String,
        val isLoadingButton: Boolean,
        val isVisibleInMenu: Boolean,
        val isVisibleInRecommendation: Boolean,
        val throwable: Throwable?,
        val selectableCategoryList: List<SelectableCategory>
    ) : BaseViewDataState {

        fun getSelectedCategory() =
            selectableCategoryList.filter { selectableCategory -> selectableCategory.selected }

        data class SelectableCategory(
            val category: Category,
            val selected: Boolean
        )
    }

    sealed interface Action : BaseAction {
        data object Init : Action
        data object OnBackClick : Action
        data class OnNameTextChanged(val name: String) : Action
        data class OnNewPriceTextChanged(val newPrice: String) : Action
        data class OnOldPriceTextChanged(val oldPrice: String) : Action
        data class OnNutritionTextChanged(val nutrition: String) : Action
        data class OnUtilsTextChanged(val utils: String) : Action
        data class OnDescriptionTextChanged(val description: String) : Action
        data class OnComboDescriptionTextChanged(val comboDescription: String) : Action
        data object OnCreateMenuProductClick : Action
        data object OnAddPhotoClick : Action
        data object OnSelectCategoriesClick : Action
        data class OnVisibleInMenuChangeClick(val isVisible: Boolean) : Action
        data class OnRecommendationVisibleChangeClick(val isVisible: Boolean) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        data object GoToGallery : Event
        data class OpenSelectCategoriesBottomSheet(val options: List<ViewDataState.SelectableCategory>) :
            Event
    }
}
