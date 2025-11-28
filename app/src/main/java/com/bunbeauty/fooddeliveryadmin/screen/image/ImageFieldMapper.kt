package com.bunbeauty.fooddeliveryadmin.screen.image

import androidx.compose.runtime.Immutable
import androidx.core.net.toUri
import com.bunbeauty.fooddeliveryadmin.compose.element.image.ImageData
import com.bunbeauty.fooddeliveryadmin.screen.menulist.common.FieldUi
import com.bunbeauty.presentation.feature.image.EditImageFieldData

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
