package com.bunbeauty.fooddeliveryadmin.screen.menulist.editmenuproduct.mapper

import androidx.core.net.toUri
import com.bunbeauty.fooddeliveryadmin.compose.element.image.ImageData
import com.bunbeauty.fooddeliveryadmin.screen.menulist.editmenuproduct.EditMenuProductViewState
import com.bunbeauty.presentation.feature.menulist.editmenuproduct.EditMenuProduct

fun EditMenuProduct.ImageFieldData.toImageFieldUi(): EditMenuProductViewState.ImageFieldUi {
    return EditMenuProductViewState.ImageFieldUi(
        value = value?.let { image ->
            when(image) {
                is EditMenuProduct.MenuProductImage.PhotoLink -> ImageData.HttpUrl(image.value)
                is EditMenuProduct.MenuProductImage.ImageUri -> ImageData.LocalUri(image.value.toUri())
            }
        },
        isError = isError,
    )
}