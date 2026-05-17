package com.bunbeauty.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.graphics.scale
import androidx.core.net.toUri
import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.s3.S3Client
import aws.sdk.kotlin.services.s3.model.DeleteObjectRequest
import aws.sdk.kotlin.services.s3.model.ListObjectsV2Request
import aws.sdk.kotlin.services.s3.model.PutObjectRequest
import aws.smithy.kotlin.runtime.content.ByteStream
import aws.smithy.kotlin.runtime.net.url.Url
import com.bunbeauty.data.BuildConfig
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.UUID

private const val DEFAULT_BYTE_SIZE = 100 * 1024
private const val YC_ENDPOINT = "https://storage.yandexcloud.net"
private const val YC_REGION = "ru-central1"

class PhotoRepository(
    private val context: Context,
) : PhotoRepo {
    private var photoListCache: List<Photo>? = null
    private val bucket: String = BuildConfig.YC_BUCKET

    private val s3Client: S3Client by lazy {
        S3Client {
            region = YC_REGION
            endpointUrl = Url.parse(YC_ENDPOINT)
            forcePathStyle = false
            credentialsProvider =
                StaticCredentialsProvider {
                    accessKeyId = BuildConfig.YC_ACCESS_KEY
                    secretAccessKey = BuildConfig.YC_SECRET_KEY
                }
        }
    }

    override suspend fun getPhotoList(username: String): List<Photo> = photoListCache ?: fetchPhotoList(username = username)

    override suspend fun fetchPhotoList(username: String): List<Photo> {
        val prefix = "${username.lowercase()}/"
        val response =
            s3Client.listObjectsV2(
                ListObjectsV2Request {
                    bucket = this@PhotoRepository.bucket
                    this.prefix = prefix
                },
            )
        val photoList =
            response.contents
                .orEmpty()
                .mapNotNull { obj ->
                    obj.key?.let { key ->
                        Photo(url = getPublicUrl(key = key))
                    }
                }
        photoListCache = photoList
        return photoList
    }

    override suspend fun uploadPhoto(
        uri: String,
        username: String,
        width: Int,
        height: Int,
    ): Photo? {
        return withContext(Dispatchers.IO) {
            val data =
                compressPhoto(
                    uri = uri,
                    width = width,
                    height = height,
                ) ?: return@withContext null

            val key = "${username.lowercase()}/${UUID.randomUUID()}.webp"
            try {
                s3Client.putObject(
                    PutObjectRequest {
                        bucket = this@PhotoRepository.bucket
                        this.key = key
                        body = ByteStream.fromBytes(data)
                        contentType = "image/webp"
                    },
                )
                photoListCache = null
                Photo(url = getPublicUrl(key = key))
            } catch (_: Throwable) {
                null
            }
        }
    }

    override suspend fun deletePhoto(photoLink: String) {
        val key = extractKey(photoLink = photoLink) ?: return
        s3Client.deleteObject(
            DeleteObjectRequest {
                bucket = this@PhotoRepository.bucket
                this.key = key
            },
        )
        photoListCache = null
    }

    override fun clearCache() {
        photoListCache = null
    }

    private fun compressPhoto(
        uri: String,
        width: Int,
        height: Int,
    ): ByteArray? {
        val photoUri = uri.toUri()
        val bitmap = photoUri.toBitmap() ?: return null
        val scaledBitmap = bitmap.scale(width, height)

        var quality = 100
        var resultByteArray: ByteArray
        val compressFormat = getCompressFormat()
        do {
            val byteArrayOutputStream = ByteArrayOutputStream()
            scaledBitmap.compress(compressFormat, quality, byteArrayOutputStream)
            resultByteArray = byteArrayOutputStream.toByteArray()

            quality -= 3
        } while (resultByteArray.size > DEFAULT_BYTE_SIZE && quality > 5)

        return resultByteArray
    }

    private fun Uri.toBitmap(): Bitmap? =
        context.contentResolver.openInputStream(this).use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }

    private fun getCompressFormat(): Bitmap.CompressFormat =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Bitmap.CompressFormat.WEBP_LOSSY
        } else {
            @Suppress("DEPRECATION")
            Bitmap.CompressFormat.WEBP
        }

    private fun getPublicUrl(key: String): String {
        val encodedKey =
            key
                .split('/')
                .joinToString(separator = "/") { segment ->
                    URLEncoder.encode(segment, Charsets.UTF_8.name()).replace("+", "%20")
                }
        return "https://$bucket.storage.yandexcloud.net/$encodedKey"
    }

    private fun extractKey(photoLink: String): String? {
        val virtualHostPrefix = "https://$bucket.storage.yandexcloud.net/"
        val pathStylePrefix = "$YC_ENDPOINT/$bucket/"
        val rawKey =
            when {
                photoLink.startsWith(virtualHostPrefix) -> photoLink.removePrefix(virtualHostPrefix)
                photoLink.startsWith(pathStylePrefix) -> photoLink.removePrefix(pathStylePrefix)
                else -> null
            }
        if (rawKey == null) return null
        return URLDecoder.decode(rawKey, Charsets.UTF_8.name())
    }
}
