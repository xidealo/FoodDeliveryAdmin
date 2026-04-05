package com.bunbeauty.presentation.feature.menulist.cropimage

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface PlatformCropImageController {
    fun setImageUri(uri: String)

    fun rotateImage()

    fun cropImage()
}

@Composable
expect fun rememberPlatformCropImageController(onImageCropped: (String) -> Unit): PlatformCropImageController

@Composable
expect fun PlatformCropImageView(
    imageUri: String,
    controller: PlatformCropImageController,
    preset: CropImagePreset,
    modifier: Modifier = Modifier,
)
