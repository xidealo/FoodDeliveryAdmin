package com.bunbeauty.presentation.feature.menulist.editmenuproduct

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.feature.image.ImageFieldData
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.toCardFieldUi
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.toTextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.error_common_menu_product_empty_description
import fooddeliveryadmin.presentation.generated.resources.error_common_menu_product_empty_name
import fooddeliveryadmin.presentation.generated.resources.error_common_menu_product_empty_new_price
import fooddeliveryadmin.presentation.generated.resources.error_common_menu_product_long_description
import fooddeliveryadmin.presentation.generated.resources.error_common_menu_product_low_old_price
import fooddeliveryadmin.presentation.generated.resources.error_common_menu_product_nutrition_without_units
import fooddeliveryadmin.presentation.generated.resources.error_common_something_went_wrong

@Immutable
data class EditMenuProductViewState(
    val title: String,
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val nameField: TextFieldUi,
            val newPriceField: TextFieldUi,
            val oldPriceField: TextFieldUi,
            val nutritionField: TextFieldUi,
            val utils: String,
            val descriptionField: TextFieldUi,
            val comboDescription: String,
            val categoriesField: CardFieldUi,
            val additionListField: CardFieldUi,
            val isVisibleInMenu: Boolean,
            val isVisibleInRecommendation: Boolean,
            val imageField: EditImageFieldUi,
            val sendingToServer: Boolean,
        ) : State
    }

    @Immutable
    data class TextFieldUi(
        val value: String,
        val isError: Boolean,
        val errorResId: String,
    )

    @Immutable
    data class CardFieldUi(
        val labelResId: String,
        val value: String,
        val isError: Boolean,
        val errorResId: String,
    )

    @Immutable
    data class EditImageFieldUi(
        val value: Any?,
        val isError: Boolean,
        val isSelected: Boolean,
    )
}

@Composable
internal fun EditMenuProduct.DataState.toViewState(): EditMenuProductViewState =
    EditMenuProductViewState(
        title = productName,
        state =
            when (state) {
                EditMenuProduct.DataState.State.LOADING -> EditMenuProductViewState.State.Loading
                EditMenuProduct.DataState.State.ERROR -> EditMenuProductViewState.State.Error
                EditMenuProduct.DataState.State.SUCCESS -> {
                    EditMenuProductViewState.State.Success(
                        nameField = nameField.toTextFieldUi(errorResId = Res.string.error_common_menu_product_empty_name),
                        newPriceField = newPriceField.toTextFieldUi(errorResId = Res.string.error_common_menu_product_empty_new_price),
                        oldPriceField = oldPriceField.toTextFieldUi(errorResId = Res.string.error_common_menu_product_low_old_price),
                        descriptionField =
                            descriptionField.toTextFieldUi(
                                errorResId =
                                    when (descriptionStateError) {
                                        EditMenuProduct.DataState.DescriptionStateError.EMPTY_DESCRIPTION_ERROR ->
                                            Res.string.error_common_menu_product_empty_description

                                        EditMenuProduct.DataState.DescriptionStateError.LONG_DESCRIPTION_ERROR ->
                                            Res.string.error_common_menu_product_long_description

                                        EditMenuProduct.DataState.DescriptionStateError.NO_ERROR ->
                                            Res.string.error_common_something_went_wrong
                                    },
                            ),
                        nutritionField = nutritionField.toTextFieldUi(
                            errorResId = Res.string.error_common_menu_product_nutrition_without_units
                        ),
                        comboDescription = comboDescription,
                        utils = units,
                        categoriesField = categoriesField.toCardFieldUi(),
                        additionListField = additionGroupListField.toCardFieldUi(),
                        isVisibleInMenu = isVisibleInMenu,
                        isVisibleInRecommendation = isVisibleInRecommendations,
                        imageField = imageField.toEditImageFieldUi(),
                        sendingToServer = sendingToServer,
                    )
                }
            },
    )

internal fun ImageFieldData.toEditImageFieldUi(): EditMenuProductViewState.EditImageFieldUi =
    EditMenuProductViewState.EditImageFieldUi(
        value = this.value,
        isError = this.isError,
        isSelected = this.value != null,
    )
