package com.bunbeauty.presentation.feature.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import coil3.compose.LocalPlatformContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

private const val IMAGE_MIME_TYPE = "image/*"
private const val MAX_PICKED_IMAGE_SIZE = 2048

@Composable
actual fun rememberImagePickerLauncher(onImagePicked: (String) -> Unit): () -> Unit {
    val context = LocalPlatformContext.current
    val coroutineScope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
        ) { uri ->
            uri?.let { selectedUri ->
                coroutineScope.launch {
                    val preparedUri =
                        prepareImageForCropping(
                            context = context,
                            uri = selectedUri,
                        )
                    onImagePicked(preparedUri)
                }
            }
        }

    return remember(launcher) {
        { launcher.launch(IMAGE_MIME_TYPE) }
    }
}

private suspend fun prepareImageForCropping(
    context: Context,
    uri: Uri,
): String =
    withContext(Dispatchers.IO) {
        val bounds =
            BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }

        context.contentResolver.openInputStream(uri).use { inputStream ->
            BitmapFactory.decodeStream(inputStream, null, bounds)
        }

        val sampleSize = calculateInSampleSize(bounds.outWidth, bounds.outHeight)
        val bitmapOptions =
            BitmapFactory.Options().apply {
                inSampleSize = sampleSize
            }

        val bitmap =
            context.contentResolver.openInputStream(uri).use { inputStream ->
                BitmapFactory.decodeStream(inputStream, null, bitmapOptions)
            } ?: return@withContext uri.toString()

        val outputFile = File(context.cacheDir, "picked_${System.currentTimeMillis()}.jpg")
        FileOutputStream(outputFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
        }
        bitmap.recycle()

        Uri.fromFile(outputFile).toString()
    }

private fun calculateInSampleSize(
    width: Int,
    height: Int,
): Int {
    var sampleSize = 1
    var halfWidth = width / 2
    var halfHeight = height / 2

    while (halfWidth / sampleSize >= MAX_PICKED_IMAGE_SIZE || halfHeight / sampleSize >= MAX_PICKED_IMAGE_SIZE) {
        sampleSize *= 2
    }

    return sampleSize
}
