package com.bunbeauty.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
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

private const val DEFAULT_WIDTH = 1000
private const val DEFAULT_HEIGHT = 667
private const val DEFAULT_BYTE_SIZE = 100 * 1024

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
            val data = compressPhoto(uri) ?: return@withContext null
            val uuid = UUID.randomUUID()
            val uploadReference = FirebaseStorage.getInstance().reference.child("$username/$uuid.webp")
            val uploadTask = uploadReference.putBytes(data)
            uploadTask.await().metadata?.reference?.let { reference ->
                Photo(url = reference.downloadUrl.await().toString())
            }
        }
    }

    override suspend fun deletePhoto(photoLink: String) {
        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(photoLink)
        storageRef.delete().await()
    }

    override suspend fun clearCache() {
        photoListCache = null
    }

    private fun compressPhoto(uri: String): ByteArray? {
        val photoUri = uri.toUri()
        val bitmap = photoUri.toBitmap() ?: return null
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, DEFAULT_WIDTH, DEFAULT_HEIGHT, true)

        var quality = 100
        var resultByteArray: ByteArray
        val compressFormat = getCompressFormat()
        do {
            val byteArrayOutputStream = ByteArrayOutputStream()
            scaledBitmap.compress(compressFormat, quality, byteArrayOutputStream)
            resultByteArray = byteArrayOutputStream.toByteArray()

            quality -= 3
        } while (resultByteArray.size > DEFAULT_BYTE_SIZE && quality > 0)

        return resultByteArray
    }

    private fun Uri.toBitmap(): Bitmap? {
        return context.contentResolver.openInputStream(this).use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    }

    private fun getCompressFormat(): Bitmap.CompressFormat {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            Bitmap.CompressFormat.WEBP
        }
    }
}
