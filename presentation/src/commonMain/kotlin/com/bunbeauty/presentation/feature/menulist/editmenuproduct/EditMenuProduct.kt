package com.bunbeauty.presentation.feature.menulist.editmenuproduct

import com.bunbeauty.presentation.feature.image.EditImageFieldData
import com.bunbeauty.presentation.feature.menulist.common.AdditionGroupListFieldData
import com.bunbeauty.presentation.feature.menulist.common.CategoriesFieldData
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface EditMenuProduct {
    data class DataState(
        val state: State,
        val productUuid: String,
        val productName: String,
        val nameField: TextFieldData,
        val newPriceField: TextFieldData,
        val oldPriceField: TextFieldData,
        val nutritionField: TextFieldData,
        val units: String,
        val descriptionField: TextFieldData,
        val descriptionStateError: DescriptionStateError,
        val comboDescription: String,
        val categoriesField: CategoriesFieldData,
        val additionGroupListField: AdditionGroupListFieldData,
        val isVisibleInMenu: Boolean,
        val isVisibleInRecommendations: Boolean,
        val imageField: EditImageFieldData,
        val sendingToServer: Boolean,
    ) : BaseDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR,
        }

        enum class DescriptionStateError {
            EMPTY_DESCRIPTION_ERROR,
            LONG_DESCRIPTION_ERROR,
            NO_ERROR,
        }
    }

    sealed interface Action : BaseAction {
        data class LoadData(
            val productUuid: String,
        ) : Action

        data object BackClick : Action

        data class ChangeNameText(
            val name: String,
        ) : Action

        data class ChangeNewPriceText(
            val newPrice: String,
        ) : Action

        data class ChangeOldPriceText(
            val oldPrice: String,
        ) : Action

        data class ChangeNutritionText(
            val nutrition: String,
        ) : Action

        data class ChangeUtilsText(
            val units: String,
        ) : Action

        data class ChangeDescriptionText(
            val description: String,
        ) : Action

        data class ChangeComboDescriptionText(
            val comboDescription: String,
        ) : Action

        data object CategoriesClick : Action

        data object AdditionListClick : Action

        data class SelectCategories(
            val categoryUuidList: List<String>,
        ) : Action

        data object ToggleVisibilityInMenu : Action

        data object ToggleVisibilityInRecommendations : Action

        data class SetImage(
            val croppedImageUri: String,
        ) : Action

        data object SaveMenuProductClick : Action
    }

    sealed interface Event : BaseEvent {
        data object NavigateBack : Event

        data class NavigateToCategoryList(
            val selectedCategoryList: List<String>,
        ) : Event

        data class NavigateToAdditionList(
            val menuProductUuid: String,
        ) : Event

        data class ShowUpdateProductSuccess(
            val productName: String,
        ) : Event

        data object ShowImageUploadingFailed : Event

        data object ShowSomethingWentWrong : Event

        data object ShowEmptyPhoto : Event
    }
}
