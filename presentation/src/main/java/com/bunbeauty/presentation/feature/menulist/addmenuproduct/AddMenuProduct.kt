package com.bunbeauty.presentation.feature.menulist.addmenuproduct

import com.bunbeauty.domain.model.Category
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface AddMenuProduct {
    data class DataState(
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
        val photoLink: String,
        val hasPhotoLinkError: Boolean,
        val isLoadingButton: Boolean,
        val isVisibleInMenu: Boolean,
        val isVisibleInRecommendation: Boolean,
        val hasError: Boolean?,
        val selectableCategoryList: List<SelectableCategory>,
        val hasCategoriesError: Boolean
    ) : BaseDataState {

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
        data object OnShowCategoryListClick : Action
        data class OnVisibleInMenuChangeClick(val isVisible: Boolean) : Action
        data class OnRecommendationVisibleChangeClick(val isVisible: Boolean) : Action
        data class SelectCategoryList(val categoryUuidList: List<String>) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        data object GoToGallery : Event
        data class GoToCategoryList(val selectedCategoryList: List<String>) :
            Event
    }
}
