package com.bunbeauty.shared.feature.menulist.createmenuproduct

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.shared.designsystem.compose.CardFieldUi
import com.bunbeauty.shared.designsystem.compose.TextFieldUi
import com.bunbeauty.shared.designsystem.compose.element.image.ImageData
import com.bunbeauty.shared.feature.image.ImageFieldData
import com.bunbeauty.shared.feature.menulist.common.CategoriesFieldData
import com.bunbeauty.shared.feature.menulist.common.TextFieldData
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import common.Constants
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.error_common_menu_product_categories
import fooddeliveryadmin.shared.generated.resources.error_common_menu_product_empty_description
import fooddeliveryadmin.shared.generated.resources.error_common_menu_product_empty_name
import fooddeliveryadmin.shared.generated.resources.error_common_menu_product_empty_new_price
import fooddeliveryadmin.shared.generated.resources.error_common_menu_product_long_description
import fooddeliveryadmin.shared.generated.resources.error_common_menu_product_low_old_price
import fooddeliveryadmin.shared.generated.resources.error_common_menu_product_nutrition_without_units
import fooddeliveryadmin.shared.generated.resources.error_common_something_went_wrong
import fooddeliveryadmin.shared.generated.resources.hint_common_menu_product_categories
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Immutable
data class CreateMenuProductViewState(
    val nameField: TextFieldUi,
    val newPriceField: TextFieldUi,
    val oldPriceField: TextFieldUi,
    val nutritionField: TextFieldUi,
    val utils: String,
    val descriptionField: TextFieldUi,
    val comboDescription: String,
    val categoriesField: CardFieldUi,
    val isVisibleInMenu: Boolean,
    val isVisibleInRecommendation: Boolean,
    val imageField: ImageFieldUi,
    val sendingToServer: Boolean,
) : BaseViewState

@Immutable
data class ImageFieldUi(
    val value: ImageData?,
    val isError: Boolean,
    val isSelected: Boolean,
) {
    companion object {
        val empty = ImageFieldUi(value = null, isError = false, isSelected = false)
    }
}

@Composable
internal fun CreateMenuProduct.DataState.toViewState(): CreateMenuProductViewState =
    CreateMenuProductViewState(
        nameField = nameField.toTextFieldUi(errorResId = Res.string.error_common_menu_product_empty_name),
        newPriceField = newPriceField.toTextFieldUi(errorResId = Res.string.error_common_menu_product_empty_new_price),
        oldPriceField = oldPriceField.toTextFieldUi(errorResId = Res.string.error_common_menu_product_low_old_price),
        descriptionField =
            descriptionField.toTextFieldUi(
                errorResId =
                    when (descriptionStateError) {
                        CreateMenuProduct.DataState.DescriptionStateError.EMPTY_DESCRIPTION_ERROR ->
                            Res.string.error_common_menu_product_empty_description

                        CreateMenuProduct.DataState.DescriptionStateError.LONG_DESCRIPTION_ERROR ->
                            Res.string.error_common_menu_product_long_description

                        CreateMenuProduct.DataState.DescriptionStateError.NO_ERROR ->
                            Res.string.error_common_something_went_wrong
                    },
            ),
        nutritionField = nutritionField.toTextFieldUi(errorResId = Res.string.error_common_menu_product_nutrition_without_units),
        comboDescription = comboDescription,
        utils = units,
        categoriesField = categoriesField.toCardFieldUi(),
        isVisibleInMenu = isVisibleInMenu,
        isVisibleInRecommendation = isVisibleInRecommendations,
        imageField = imageField.toImageFieldUi(),
        sendingToServer = sendingToServer,
    )

fun TextFieldData.toTextFieldUi(errorResId: StringResource): TextFieldUi =
    TextFieldUi(
        value = value,
        isError = isError,
        errorResId = errorResId,
    )

@Composable
fun CategoriesFieldData.toCardFieldUi(): CardFieldUi =
    CardFieldUi(
        labelResId = stringResource(Res.string.hint_common_menu_product_categories),
        value =
            selectedCategoryList
                .takeIf { list ->
                    list.isNotEmpty()
                }?.joinToString(" ${Constants.BULLET_SYMBOL} ") { selectableCategory ->
                    selectableCategory.category.name
                },
        isError = isError,
        errorResId = Res.string.error_common_menu_product_categories,
    )

fun ImageFieldData.toImageFieldUi(): ImageFieldUi =
    ImageFieldUi(
        value =
            value?.let { imageUri ->
                ImageData.HttpUrl(
                    url = imageUri,
                )
            },
        isError = isError,
        isSelected = value != null,
    )
