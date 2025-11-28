package com.bunbeauty.presentation.feature.menulist.createmenuproduct

import com.bunbeauty.domain.feature.menu.common.model.SelectableCategory
import com.bunbeauty.presentation.feature.image.ImageFieldData
import com.bunbeauty.presentation.feature.menulist.common.CategoriesFieldData
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface CreateMenuProduct {
    data class DataState(
        val nameField: TextFieldData,
        val newPriceField: TextFieldData,
        val oldPriceField: TextFieldData,
        val nutritionField: TextFieldData,
        val units: String,
        val descriptionField: TextFieldData,
        val descriptionStateError: DescriptionStateError,
        val comboDescription: String,
        val categoriesField: CategoriesFieldData,
        val isVisibleInMenu: Boolean,
        val isVisibleInRecommendations: Boolean,
        val imageField: ImageFieldData,

        val sendingToServer: Boolean
    ) : BaseDataState {

        val selectedCategoryList: List<SelectableCategory>
            get() {
                return categoriesField.value.filter { category ->
                    category.selected
                }
            }

        enum class DescriptionStateError {
            EMPTY_DESCRIPTION_ERROR,
            LONG_DESCRIPTION_ERROR,
            NO_ERROR
        }
    }

    sealed interface Action : BaseAction {
        data object Init : Action
        data object BackClick : Action

        data class ChangeNameText(val name: String) : Action
        data class ChangeNewPriceText(val newPrice: String) : Action
        data class ChangeOldPriceText(val oldPrice: String) : Action
        data class ChangeNutritionText(val nutrition: String) : Action
        data class ChangeUnitsText(val units: String) : Action
        data class ChangeDescriptionText(val description: String) : Action
        data class ChangeComboDescriptionText(val comboDescription: String) : Action
        data object CategoriesClick : Action
        data class SelectCategories(val categoryUuidList: List<String>) : Action
        data object ToggleVisibilityInMenu : Action
        data object ToggleVisibilityInRecommendations : Action
        data class SetImage(
            val croppedImageUri: String
        ) : Action

        data object CreateMenuProductClick : Action
    }

    sealed interface Event : BaseEvent {
        data object NavigateBack : Event
        data class NavigateToCategoryList(val selectedCategoryList: List<String>) : Event
        data class ShowMenuProductCreated(val menuProductName: String) : Event
        data object ShowSomethingWentWrong : Event
        data object ShowImageUploadingFailed : Event
        data object ShowEmptyPhoto : Event
    }
}
