package com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct.mapper

import androidx.core.net.toUri
import com.bunbeauty.fooddeliveryadmin.compose.element.image.ImageData
import com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct.CreateMenuProductViewState
import com.bunbeauty.presentation.feature.menulist.createmenuproduct.CreateMenuProduct

fun CreateMenuProduct.ImageFieldData.toImageFieldUi(): CreateMenuProductViewState.ImageFieldUi {
    return CreateMenuProductViewState.ImageFieldUi(
        value = value?.let { imageUri ->
            ImageData.LocalUri(uri = imageUri.toUri())
        },
        isError = isError,
    )
}