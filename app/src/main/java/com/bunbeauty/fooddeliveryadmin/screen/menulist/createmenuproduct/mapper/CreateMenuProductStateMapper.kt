package com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct.mapper

import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.toCardFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.toTextFieldUi
import com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct.CreateMenuProductViewState
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.CreateMenuProduct

fun CreateMenuProduct.DataState.toAddMenuProductViewState(): CreateMenuProductViewState {
    return CreateMenuProductViewState(
        nameField = nameField.toTextFieldUi(errorResId = R.string.error_common_menu_product_empty_name),
        newPriceField = newPriceField.toTextFieldUi(errorResId = R.string.error_common_menu_product_empty_new_price),
        oldPriceField = oldPriceField.toTextFieldUi(errorResId = R.string.error_common_menu_product_low_old_price),
        descriptionField = descriptionField.toTextFieldUi(errorResId = R.string.error_common_menu_product_empty_description),
        nutritionField = nutritionField.toTextFieldUi(errorResId = R.string.error_common_menu_product_nutrition_without_units),
        comboDescription = comboDescription,
        utils = units,
        categoriesField = categoriesField.toCardFieldUi(),
        isVisibleInMenu = isVisibleInMenu,
        isVisibleInRecommendation = isVisibleInRecommendations,
        imageField = imageField.toImageFieldUi(),
        sendingToServer = sendingToServer
    )
}
