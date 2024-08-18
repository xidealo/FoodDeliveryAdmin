package com.bunbeauty.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.core.net.toUri
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

private const val REQUIRED_IMAGE_SIZE = 100.0

class PhotoRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : PhotoRepo {

    private var photoListCache: List<Photo>? = null

    override suspend fun getPhotoList(username: String): List<Photo> = coroutineScope {
        photoListCache ?: fetchPhotoList(username = username)
    }

    override suspend fun fetchPhotoList(username: String): List<Photo> = coroutineScope {
        val imagesRef = FirebaseStorage.getInstance().reference.child(username)

        val referenceListResult = imagesRef.listAll().await()

        val photoList = referenceListResult.items.map { reference ->
            async {
                Photo(url = reference.downloadUrl.await().toString())
            }
        }.awaitAll()
        photoListCache = photoList
        photoList
    }

    override suspend fun uploadPhoto(
        uri: String,
        username: String
    ): Photo? {
        return withContext(Dispatchers.IO) {
            val photoUri = uri.toUri()
            val bitmap = photoUri.toBitmap() ?: return@withContext null
            val compressFormat = getCompressFormat()
            val quality = photoUri.compressQuality()
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(compressFormat, quality, outputStream)
            val data = outputStream.toByteArray()

            val uuid = UUID.randomUUID()
            val uploadReference = FirebaseStorage.getInstance().reference.child("$username/$uuid.webp")
            val uploadTask = uploadReference.putBytes(data)
            uploadTask.await().metadata?.reference?.let { reference ->
                Photo(url = reference.downloadUrl.await().toString())
            }
        }
    }

    override suspend fun clearCache() {
        photoListCache = null
    }

    private fun Uri.toBitmap(): Bitmap? {
        return context.contentResolver.openInputStream(this).use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    }

    private fun Uri.compressQuality(): Int {
        return fileSize().takeIf { originalSize ->
            originalSize > 0
        }?.let { originalSize ->
            ((REQUIRED_IMAGE_SIZE / originalSize) * 100).toInt()
        }?.coerceIn(10..100) ?: 100
    }

    private fun Uri.fileSize(): Long {
        var fileSize: Long = 0
        context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst()) {
                fileSize = cursor.getLong(sizeIndex)
            }
        }

        return fileSize / 1000
    }

    private fun getCompressFormat(): Bitmap.CompressFormat {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            Bitmap.CompressFormat.WEBP
        }
    }
}
