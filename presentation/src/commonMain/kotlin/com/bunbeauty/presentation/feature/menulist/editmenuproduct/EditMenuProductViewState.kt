package com.bunbeauty.presentation.feature.menulist.editmenuproduct

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.designsystem.compose.CardFieldUi
import com.bunbeauty.presentation.designsystem.compose.TextFieldUi
import com.bunbeauty.presentation.feature.image.EditImageFieldData
import com.bunbeauty.presentation.feature.image.ImageFieldData
import com.bunbeauty.presentation.feature.menulist.common.AdditionGroupListFieldData
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.toCardFieldUi
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.toTextFieldUi
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import common.Constants
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.error_common_menu_product_categories
import fooddeliveryadmin.presentation.generated.resources.error_common_menu_product_empty_description
import fooddeliveryadmin.presentation.generated.resources.error_common_menu_product_empty_name
import fooddeliveryadmin.presentation.generated.resources.error_common_menu_product_empty_new_price
import fooddeliveryadmin.presentation.generated.resources.error_common_menu_product_long_description
import fooddeliveryadmin.presentation.generated.resources.error_common_menu_product_low_old_price
import fooddeliveryadmin.presentation.generated.resources.error_common_menu_product_nutrition_without_units
import fooddeliveryadmin.presentation.generated.resources.error_common_something_went_wrong
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_additions
import fooddeliveryadmin.presentation.generated.resources.hint_common_menu_product_categories
import org.jetbrains.compose.resources.stringResource

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


@Composable
fun AdditionGroupListFieldData.toCardFieldUi(): CardFieldUi =
    CardFieldUi(
        labelResId = stringResource(Res.string.hint_common_menu_product_additions),
        value =
            value
                .takeIf { list ->
                    list.isNotEmpty()
                }?.joinToString(" ${Constants.BULLET_SYMBOL} ") { addition ->
                    addition.name
                },
        isError = isError,
        errorResId = Res.string.error_common_menu_product_categories,
    )


internal fun EditImageFieldData.toEditImageFieldUi(): EditMenuProductViewState.EditImageFieldUi =
    EditMenuProductViewState.EditImageFieldUi(
        value = this.value,
        isError = this.isError,
        isSelected = this.value != null,
    )
