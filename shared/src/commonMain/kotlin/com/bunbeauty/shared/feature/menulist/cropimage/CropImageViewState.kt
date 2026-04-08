package com.bunbeauty.shared.feature.menulist.cropimage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.shared.viewmodel.base.BaseViewState

@Immutable
data class CropImageViewState(
    val isLoading: Boolean,
    val imageContent: ImageContent,
) : BaseViewState {
    @Immutable
    data class ImageContent(
        val uri: String?,
    )
}

@Composable
internal fun CropImage.DataState.toViewState(): CropImageViewState =
    CropImageViewState(
        isLoading = isLoading,
        imageContent =
            CropImageViewState.ImageContent(
                uri = uri,
            ),
    )
