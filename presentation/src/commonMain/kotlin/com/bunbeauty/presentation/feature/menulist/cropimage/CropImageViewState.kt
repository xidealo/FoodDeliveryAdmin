package com.bunbeauty.presentation.feature.menulist.cropimage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import coil3.Uri
import coil3.toUri
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class CropImageViewState(
    val isLoading: Boolean,
    val imageContent: ImageContent,
) : BaseViewState {
    @Immutable
    data class ImageContent(
        val uri: Uri?,
    )
}

@Composable
internal fun CropImage.DataState.toViewState(): CropImageViewState =
    CropImageViewState(
        isLoading = isLoading,
        imageContent = CropImageViewState.ImageContent(
            uri = uri?.toUri()
        ),
    )
