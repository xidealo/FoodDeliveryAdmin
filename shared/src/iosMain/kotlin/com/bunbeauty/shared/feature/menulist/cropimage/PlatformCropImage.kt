package com.bunbeauty.shared.feature.menulist.cropimage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGContextRotateCTM
import platform.CoreGraphics.CGContextTranslateCTM
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.writeToFile
import platform.UIKit.UIColor
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetCurrentContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIRectFill
import platform.UIKit.drawInRect
import kotlin.math.PI
import kotlin.time.Clock

private const val DEFAULT_ANGLE = 90f
private const val MENU_PRODUCT_ASPECT_RATIO = 1000f / 667f
private const val ADDITION_ASPECT_RATIO = 1f
private const val MENU_PRODUCT_WIDTH = 1000.0
private const val MENU_PRODUCT_HEIGHT = 667.0
private const val ADDITION_WIDTH = 240.0
private const val ADDITION_HEIGHT = 240.0

private class IosPlatformCropImageController(
    private val onImageCropped: (String) -> Unit,
) : PlatformCropImageController {
    private var imageUri: String? = null
    private var preset: CropImagePreset = CropImagePreset.MENU_PRODUCT
    var offset by mutableStateOf(Offset.Zero)
    var scale by mutableFloatStateOf(1f)
    var rotation by mutableFloatStateOf(0f)
    private var previewWidth: Int = 0
    private var previewHeight: Int = 0

    override fun setImageUri(uri: String) {
        imageUri = uri
    }

    fun setPreset(preset: CropImagePreset) {
        this.preset = preset
    }

    fun setTransform(
        offset: Offset,
        scale: Float,
        rotation: Float,
        previewWidth: Int,
        previewHeight: Int,
    ) {
        this.offset = offset
        this.scale = scale
        this.rotation = rotation
        this.previewWidth = previewWidth
        this.previewHeight = previewHeight
    }

    override fun rotateImage() {
        rotation += DEFAULT_ANGLE
    }

    override fun cropImage() {
        val uri = imageUri ?: return
        exportImage(
            uri = uri,
            preset = preset,
            offset = offset,
            scale = scale,
            rotation = rotation,
            previewWidth = previewWidth,
            previewHeight = previewHeight,
        )?.let(onImageCropped)
    }
}

@Composable
actual fun rememberPlatformCropImageController(onImageCropped: (String) -> Unit): PlatformCropImageController {
    val currentOnImageCropped = rememberUpdatedState(onImageCropped)
    return remember {
        IosPlatformCropImageController { croppedImageUri ->
            currentOnImageCropped.value(croppedImageUri)
        }
    }
}

@Composable
actual fun PlatformCropImageView(
    imageUri: String,
    controller: PlatformCropImageController,
    preset: CropImagePreset,
    modifier: Modifier,
) {
    val iosController = controller as IosPlatformCropImageController
    var previewWidth by remember(imageUri) { mutableStateOf(0) }
    var previewHeight by remember(imageUri) { mutableStateOf(0) }

    SideEffect {
        iosController.setImageUri(imageUri)
        iosController.setPreset(preset)
        iosController.setTransform(
            offset = iosController.offset,
            scale = iosController.scale,
            rotation = iosController.rotation,
            previewWidth = previewWidth,
            previewHeight = previewHeight,
        )
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(preset.aspectRatio())
                    .clipToBounds()
                    .background(Color.White)
                    .border(1.dp, Color.White)
                    .onSizeChanged { size ->
                        previewWidth = size.width
                        previewHeight = size.height
                    }.pointerInput(imageUri) {
                        detectTransformGestures { _, pan, zoom, rotate ->
                            iosController.setTransform(
                                offset = iosController.offset + pan,
                                scale = (iosController.scale * zoom).coerceIn(0.5f, 6f),
                                rotation = iosController.rotation + rotate,
                                previewWidth = previewWidth,
                                previewHeight = previewHeight,
                            )
                        }
                    },
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .matchParentSize()
                        .padding(1.dp)
                        .graphicsLayer {
                            scaleX = iosController.scale
                            scaleY = iosController.scale
                            translationX = iosController.offset.x
                            translationY = iosController.offset.y
                            rotationZ = iosController.rotation
                        },
            )
        }
    }
}

private fun CropImagePreset.aspectRatio(): Float =
    when (this) {
        CropImagePreset.MENU_PRODUCT -> MENU_PRODUCT_ASPECT_RATIO
        CropImagePreset.ADDITION -> ADDITION_ASPECT_RATIO
    }

@OptIn(ExperimentalForeignApi::class)
private fun exportImage(
    uri: String,
    preset: CropImagePreset,
    offset: Offset,
    scale: Float,
    rotation: Float,
    previewWidth: Int,
    previewHeight: Int,
): String? {
    val sourceImage = loadUIImage(uri = uri) ?: return null
    val outputWidth =
        when (preset) {
            CropImagePreset.MENU_PRODUCT -> MENU_PRODUCT_WIDTH
            CropImagePreset.ADDITION -> ADDITION_WIDTH
        }
    val outputHeight =
        when (preset) {
            CropImagePreset.MENU_PRODUCT -> MENU_PRODUCT_HEIGHT
            CropImagePreset.ADDITION -> ADDITION_HEIGHT
        }

    UIGraphicsBeginImageContextWithOptions(CGSizeMake(outputWidth, outputHeight), true, 1.0)
    UIColor.whiteColor.setFill()
    UIRectFill(CGRectMake(0.0, 0.0, outputWidth, outputHeight))

    val (sourceWidth, sourceHeight) = sourceImage.size.useContents { width to height }
    val baseScale = maxOf(outputWidth / sourceWidth, outputHeight / sourceHeight)
    val drawnWidth = sourceWidth * baseScale * scale
    val drawnHeight = sourceHeight * baseScale * scale
    val outputOffsetX = if (previewWidth > 0) offset.x.toDouble() / previewWidth * outputWidth else 0.0
    val outputOffsetY = if (previewHeight > 0) offset.y.toDouble() / previewHeight * outputHeight else 0.0

    val context = UIGraphicsGetCurrentContext() ?: return null
    CGContextTranslateCTM(context, outputWidth / 2.0 + outputOffsetX, outputHeight / 2.0 + outputOffsetY)
    CGContextRotateCTM(context, rotation.toDouble() * PI / 180.0)
    sourceImage.drawInRect(CGRectMake(-drawnWidth / 2.0, -drawnHeight / 2.0, drawnWidth, drawnHeight))

    val outputImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    val jpegData = outputImage?.let { image -> UIImageJPEGRepresentation(image, 0.95) } ?: return null

    val outputPath = NSTemporaryDirectory() + "cropped_${Clock.System.now().toEpochMilliseconds()}.jpg"
    if (!jpegData.writeToFile(outputPath, atomically = true)) return null
    return NSURL.fileURLWithPath(outputPath).absoluteString
}

@OptIn(ExperimentalForeignApi::class)
private fun loadUIImage(uri: String): UIImage? {
    val data: NSData? =
        if (uri.startsWith("file://")) {
            NSURL.URLWithString(uri)?.let { url ->
                NSData.dataWithContentsOfURL(url)
            }
        } else {
            NSData.dataWithContentsOfFile(uri)
        }
    return data?.let { imageData -> UIImage.imageWithData(imageData) }
}
