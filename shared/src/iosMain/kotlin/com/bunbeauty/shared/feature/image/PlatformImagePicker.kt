package com.bunbeauty.shared.feature.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSDate
import platform.Foundation.NSTemporaryDirectory
import platform.Foundation.NSURL
import platform.Foundation.writeToFile
import platform.UIKit.UIApplication
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UIImagePickerControllerSourceTypePhotoLibrary
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.UIWindowScene
import platform.UIKit.drawInRect
import platform.darwin.NSObject

private const val MAX_PICKED_IMAGE_SIZE = 2048.0
private const val PICKED_JPEG_QUALITY = 0.95

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberImagePickerLauncher(onImagePicked: (String) -> Unit): () -> Unit {
    val currentOnImagePicked = rememberUpdatedState(onImagePicked)
    val delegate =
        remember {
            IosImagePickerDelegate { image ->
                savePickedImage(image)?.let(currentOnImagePicked.value)
            }
        }

    return remember(delegate) {
        {
            val picker =
                UIImagePickerController().apply {
                    sourceType = UIImagePickerControllerSourceTypePhotoLibrary
                    allowsEditing = false
                    this.delegate = delegate
                }
            topViewController()?.presentViewController(picker, animated = true, completion = null)
        }
    }
}

private class IosImagePickerDelegate(
    private val onImagePicked: (UIImage) -> Unit,
) : NSObject(),
    UIImagePickerControllerDelegateProtocol,
    UINavigationControllerDelegateProtocol {
    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>,
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        picker.dismissViewControllerAnimated(true, completion = null)
        image?.let(onImagePicked)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, completion = null)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun savePickedImage(image: UIImage): String? {
    val jpegData = UIImageJPEGRepresentation(image.scaledDownIfNeeded(), PICKED_JPEG_QUALITY) ?: return null
    val outputPath = NSTemporaryDirectory() + "picked_${NSDate().timeIntervalSince1970}.jpg"
    if (!jpegData.writeToFile(outputPath, atomically = true)) return null
    return NSURL.fileURLWithPath(outputPath).absoluteString
}

@OptIn(ExperimentalForeignApi::class)
private fun UIImage.scaledDownIfNeeded(): UIImage {
    val (sourceWidth, sourceHeight) = size.useContents { width to height }
    val maxDimension = maxOf(sourceWidth, sourceHeight)
    if (maxDimension <= MAX_PICKED_IMAGE_SIZE) return this

    val scaleFactor = MAX_PICKED_IMAGE_SIZE / maxDimension
    val targetWidth = sourceWidth * scaleFactor
    val targetHeight = sourceHeight * scaleFactor
    UIGraphicsBeginImageContextWithOptions(CGSizeMake(targetWidth, targetHeight), true, 1.0)
    drawInRect(CGRectMake(0.0, 0.0, targetWidth, targetHeight))
    val scaledImage = UIGraphicsGetImageFromCurrentImageContext()
    UIGraphicsEndImageContext()
    return scaledImage ?: this
}

private fun topViewController(): UIViewController? {
    val sceneRootViewController =
        UIApplication.sharedApplication.connectedScenes
            .mapNotNull { scene -> scene as? UIWindowScene }
            .flatMap { windowScene -> windowScene.windows.mapNotNull { window -> window as? UIWindow } }
            .firstOrNull { window -> window.keyWindow }
            ?.rootViewController

    var topController = sceneRootViewController ?: UIApplication.sharedApplication.keyWindow?.rootViewController
    while (topController?.presentedViewController != null) {
        topController = topController.presentedViewController
    }
    return topController
}
