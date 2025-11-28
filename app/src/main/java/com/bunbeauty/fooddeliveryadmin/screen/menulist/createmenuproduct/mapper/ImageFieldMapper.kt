package com.bunbeauty.fooddeliveryadmin.screen.menulist.createmenuproduct.mapper

import androidx.core.net.toUri
import com.bunbeauty.fooddeliveryadmin.compose.element.image.ImageData
import com.bunbeauty.fooddeliveryadmin.screen.image.ImageFieldUi
import com.bunbeauty.presentation.feature.image.ImageFieldData

fun ImageFieldData.toImageFieldUi(): ImageFieldUi =
    ImageFieldUi(
        value =
            value?.let { imageUri ->
                ImageData.LocalUri(uri = imageUri.toUri())
            },
        isError = isError,
    )
