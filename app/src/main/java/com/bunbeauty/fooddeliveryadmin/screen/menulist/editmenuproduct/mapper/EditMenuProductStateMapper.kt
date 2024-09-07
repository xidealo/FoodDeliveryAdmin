package com.bunbeauty.fooddeliveryadmin.screen.menulist.editmenuproduct.mapper

import com.bunbeauty.fooddeliveryadmin.R
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
                descriptionField = descriptionField.toTextFieldUi(errorResId = R.string.error_common_menu_product_empty_description),
                nutrition = nutrition,
                comboDescription = comboDescription,
                utils = utils,
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
