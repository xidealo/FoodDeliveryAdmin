package com.bunbeauty.fooddeliveryadmin.screen.menulist.addmenuproduct

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bunbeauty.common.Constants
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.presentation.feature.menulist.addmenuproduct.AddMenuProduct

@Composable
fun AddMenuProduct.DataState.toAddMenuProductViewState(): AddMenuProductViewState {
    return AddMenuProductViewState(
        hasError = hasError,
        name = name,
        nameError = if (hasNameError) {
            R.string.error_add_menu_product_empty_name
        } else {
            null
        },
        newPrice = newPrice,
        newPriceError = if (hasNewPriceError) {
            R.string.error_add_menu_product_empty_new_price
        } else {
            null
        },
        oldPrice = oldPrice,
        oldPriceError = if (hasOldPriceError) {
            R.string.error_add_menu_product_old_price_incorrect
        } else {
            null
        },
        description = description,
        descriptionError = if (hasDescriptionError) {
            R.string.error_add_menu_product_empty_description
        } else {
            null
        },
        nutrition = nutrition,
        comboDescription = comboDescription,
        isLoadingButton = false,
        utils = utils,
        categoryLabel = if (getSelectedCategory().isEmpty()) {
            stringResource(id = R.string.title_add_menu_product_categories)
        } else {
            getSelectedCategory()
                .joinToString(" ${Constants.BULLET_SYMBOL} ") { category ->
                    category.category.name
                }
        },
        categoryHint = if (getSelectedCategory().isEmpty()) {
            null
        } else {
            R.string.hint_add_menu_product_categories
        },
        isVisibleInMenu = isVisibleInMenu,
        isVisibleInRecommendation = isVisibleInRecommendation,
        photoErrorBorder = if (hasPhotoLinkError) {
            BorderStroke(width = 2.dp, color = AdminTheme.colors.main.error)
        } else {
            null
        },
        photoContainsColor = if (hasPhotoLinkError) {
            AdminTheme.colors.main.error
        } else {
            AdminTheme.colors.main.onSurface
        },
        categoriesErrorBorder = if (hasCategoriesError) {
            BorderStroke(width = 2.dp, color = AdminTheme.colors.main.error)
        } else {
            null
        },
        selectableCategoryList = selectableCategoryList.map { selectableCategory ->
            AddMenuProductViewState.CategoryItem(
                uuid = selectableCategory.category.uuid,
                name = selectableCategory.category.name,
                selected = selectableCategory.selected
            )
        }
    )
}
