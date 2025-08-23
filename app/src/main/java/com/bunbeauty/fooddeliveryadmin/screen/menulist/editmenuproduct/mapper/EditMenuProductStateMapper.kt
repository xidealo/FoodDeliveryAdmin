package com.bunbeauty.fooddeliveryadmin.screen.menulist.editmenuproduct.mapper

import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.screen.image.toImageFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.toCardFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.toTextFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.editmenuproduct.EditMenuProductViewState
import com.bunbeauty.presentation.feature.menulist.editmenuproduct.EditMenuProduct

fun EditMenuProduct.DataState.toEditMenuProductViewState(): EditMenuProductViewState {
    return EditMenuProductViewState(
        title = productName,
        state = when (state) {
            EditMenuProduct.DataState.State.SUCCESS -> EditMenuProductViewState.State.Success(
                nameField = nameField.toTextFieldUi(errorResId = R.string.error_common_menu_product_empty_name),
                newPriceField = newPriceField.toTextFieldUi(errorResId = R.string.error_common_menu_product_empty_new_price),
                oldPriceField = oldPriceField.toTextFieldUi(errorResId = R.string.error_common_menu_product_low_old_price),
                descriptionField = descriptionField.toTextFieldUi(
                    errorResId = when (descriptionStateError) {
                        EditMenuProduct.DataState.DescriptionStateError.EMPTY_DESCRIPTION_ERROR -> R.string.error_common_menu_product_empty_description
                        EditMenuProduct.DataState.DescriptionStateError.LONG_DESCRIPTION_ERROR -> R.string.error_common_menu_product_long_description
                        EditMenuProduct.DataState.DescriptionStateError.NO_ERROR -> R.string.error_common_something_went_wrong
                    }
                ),
                nutritionField = nutritionField.toTextFieldUi(errorResId = R.string.error_common_menu_product_nutrition_without_units),
                comboDescription = comboDescription,
                utils = units,
                categoriesField = categoriesField.toCardFieldUi(),
                isVisibleInMenu = isVisibleInMenu,
                isVisibleInRecommendation = isVisibleInRecommendations,
                imageField = imageField.toImageFieldUi(),
                sendingToServer = sendingToServer
            )

            EditMenuProduct.DataState.State.ERROR -> EditMenuProductViewState.State.Error
            EditMenuProduct.DataState.State.LOADING -> EditMenuProductViewState.State.Loading
        }

    )
}
