package com.bunbeauty.presentation.feature.menulist.cropimage

import android.widget.LinearLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import androidx.core.net.toUri
import com.canhub.cropper.CropImageView.OnCropImageCompleteListener

private const val DEFAULT_ANGLE = 90
private const val ORIGINAL_QUALITY = 100
private const val CROP_WIDTH = 1000
private const val CROP_HEIGHT = 667

private class AndroidPlatformCropImageController(
    private val onImageCropped: (String) -> Unit,
) : PlatformCropImageController {
    private var cropImageView: CropImageView? = null

    fun bind(view: CropImageView) {
        if (cropImageView === view) return

        cropImageView = view
        view.setOnCropImageCompleteListener { _, result ->
            result.uriContent?.toString()?.let(onImageCropped)
        }
    }

    override fun setImageUri(uri: String) = Unit

    override fun rotateImage() {
        cropImageView?.rotateImage(DEFAULT_ANGLE)
    }

    override fun cropImage() {
        cropImageView?.croppedImageAsync(
            saveCompressQuality = ORIGINAL_QUALITY,
            reqWidth = CROP_WIDTH,
            reqHeight = CROP_HEIGHT,
        )
    }
}

@Composable
actual fun rememberPlatformCropImageController(
    onImageCropped: (String) -> Unit,
): PlatformCropImageController =
    remember(onImageCropped) {
        AndroidPlatformCropImageController(onImageCropped)
    }

@Composable
actual fun PlatformCropImageView(
    imageUri: String,
    controller: PlatformCropImageController,
    modifier: Modifier,
) {
    val androidController = controller as AndroidPlatformCropImageController

    CropImageView(
        imageContent = imageUri,
        cropImageDefaults = CropImageDefaults.menuProductOptions(),
        androidController = androidController,
        onImageCropped = {

        }
    )
}

@Composable
private fun CropImageView(
    imageContent: String,
    cropImageDefaults: CropImageOptions,
    androidController: AndroidPlatformCropImageController,
    onImageCropped: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uri = imageContent.toUri()

    val callback = remember {
        object : OnCropImageCompleteListener {
            override fun onCropImageComplete(
                view: CropImageView,
                result: CropImageView.CropResult
            ) {
                onImageCropped(result.uriContent.toString())
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.align(Alignment.Center),
            factory = { context ->
                LinearLayout(context).apply {
                    var cropImageView =
                        CropImageView(context)
                            .apply {
                                setImageCropOptions(cropImageDefaults)
                                setImageUriAsync(uri)
                            }.also { view ->
                                view.setOnCropImageCompleteListener(callback)
                            }
                    addView(cropImageView)
                }
            },
        )
    }
}