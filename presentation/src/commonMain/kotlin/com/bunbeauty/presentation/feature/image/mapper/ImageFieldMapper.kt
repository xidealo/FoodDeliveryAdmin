package com.bunbeauty.presentation.feature.image.mapper

import coil3.toUri
import com.bunbeauty.presentation.designsystem.compose.element.image.ImageData
import com.bunbeauty.presentation.feature.image.ImageFieldData
import com.bunbeauty.presentation.feature.image.ImageFieldUi

fun ImageFieldData.toImageFieldUi(): ImageFieldUi =
    ImageFieldUi(
        value =
            value?.let { imageUri ->
                ImageData.LocalUri(uri = imageUri.toUri())
            },
        isError = isError,
    )
