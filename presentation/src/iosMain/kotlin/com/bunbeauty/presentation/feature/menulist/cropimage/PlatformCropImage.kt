package com.bunbeauty.presentation.feature.menulist.cropimage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

private class IosPlatformCropImageController : PlatformCropImageController {
    override fun setImageUri(uri: String) = Unit

    override fun rotateImage() = Unit

    override fun cropImage() = Unit
}

@Composable
actual fun rememberPlatformCropImageController(
    onImageCropped: (String) -> Unit,
): PlatformCropImageController =
    remember {
        IosPlatformCropImageController()
    }

@Composable
actual fun PlatformCropImageView(
    imageUri: String,
    controller: PlatformCropImageController,
    preset: CropImagePreset,
    modifier: Modifier,
) {
    Box(modifier = modifier.fillMaxSize())
}
