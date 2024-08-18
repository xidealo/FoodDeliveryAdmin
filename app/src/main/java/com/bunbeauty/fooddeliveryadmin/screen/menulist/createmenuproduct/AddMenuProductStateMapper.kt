package com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.bunbeauty.common.Constants
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.element.image.ImageData
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.presentation.feature.menulist.addmenuproduct.AddMenuProduct

@Composable
fun AddMenuProduct.DataState.toAddMenuProductViewState(): AddMenuProductViewState {
    return AddMenuProductViewState(
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
        utils = utils,
        categoryLabel = if (selectedCategoryList.isEmpty()) {
            stringResource(id = R.string.title_add_menu_product_categories)
        } else {
            selectedCategoryList.joinToString(" ${Constants.BULLET_SYMBOL} ") { category ->
                category.category.name
            }
        },
        categoryHint = if (selectedCategoryList.isEmpty()) {
            null
        } else {
            R.string.hint_add_menu_product_categories
        },
        isVisibleInMenu = isVisibleInMenu,
        isVisibleInRecommendation = isVisibleInRecommendation,
        categoriesBorder = if (hasCategoriesError) {
            BorderStroke(width = 2.dp, color = AdminTheme.colors.main.error)
        } else {
            null
        },
        selectableCategoryList = selectedCategoryList.map { selectableCategory ->
            AddMenuProductViewState.CategoryItem(
                uuid = selectableCategory.category.uuid,
                name = selectableCategory.category.name,
                selected = selectableCategory.selected
            )
        },
        imageUris = run {
            val original = originalImageUri ?: return@run null
            val cropped = croppedImageUri ?: return@run null
            AddMenuProductViewState.ImageUris(
                originalImageUri = original.toUri(),
                croppedImageData =  ImageData.LocalUri(uri = cropped.toUri()),
            )
        },
        imageError = hasImageError,
        sendingToServer = sendingToServer,
    )
}
