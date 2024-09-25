package com.bunbeauty.fooddeliveryadmin.screen.menulist.editmenuproduct.mapper

import androidx.core.net.toUri
import com.bunbeauty.fooddeliveryadmin.compose.element.image.ImageData
import com.bunbeauty.fooddeliveryadmin.screen.menulist.editmenuproduct.EditMenuProductViewState
import com.bunbeauty.presentation.feature.menulist.editmenuproduct.EditMenuProduct

fun EditMenuProduct.ImageFieldData.toImageFieldUi(): EditMenuProductViewState.ImageFieldUi {
    return EditMenuProductViewState.ImageFieldUi(
        value = value?.let { image ->
            val newImageUri = image.newImageUri
            when {
                newImageUri != null -> ImageData.LocalUri(newImageUri.toUri())
                else -> ImageData.HttpUrl(image.photoLink)
            }
        },
        isError = isError
    )
}
