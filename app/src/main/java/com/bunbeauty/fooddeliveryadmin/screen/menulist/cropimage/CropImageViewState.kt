package com.bunbeauty.fooddeliveryadmin.screen.menulist.cropimage

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.bunbeauty.presentation.viewmodel.base.BaseViewState

@Immutable
data class CropImageViewState(
    val isLoading: Boolean,
    val imageContent: ImageContent
) : BaseViewState

@Immutable
data class ImageContent(
    val uri: Uri?
)
