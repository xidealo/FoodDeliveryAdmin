package com.bunbeauty.presentation.feature.image

import androidx.compose.runtime.Immutable
import coil3.toUri
import com.bunbeauty.presentation.designsystem.compose.element.image.ImageData
import com.bunbeauty.presentation.feature.common.FieldUi

fun EditImageFieldData.toImageFieldUi(): ImageFieldUi =
    ImageFieldUi(
        value =
            value?.let { image ->
                val newImageUri = image.newImageUri
                when {
                    newImageUri != null -> ImageData.LocalUri(newImageUri.toUri())
                    else -> ImageData.HttpUrl(image.photoLink)
                }
            },
        isError = isError,
    )

@Immutable
data class ImageFieldUi(
    override val value: ImageData?,
    override val isError: Boolean,
) : FieldUi<ImageData?>() {
    val isSelected: Boolean = value != null
}
