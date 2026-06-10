package com.bunbeauty.data.repository

import com.bunbeauty.data.storage.YandexS3KtorClient
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIGraphicsBeginImageContextWithOptions
import platform.UIKit.UIGraphicsEndImageContext
import platform.UIKit.UIGraphicsGetImageFromCurrentImageContext
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.drawInRect

private const val DEFAULT_BYTE_SIZE = 100 * 1024
private const val MIN_JPEG_QUALITY = 0.05
private const val JPEG_QUALITY_STEP = 0.03

class IosPhotoRepository(
    private val yandexS3Client: YandexS3KtorClient,
) : PhotoRepo {
    private var photoListCache: List<Photo>? = null

    override suspend fun getPhotoList(username: String): List<Photo> = photoListCache ?: fetchPhotoList(username = username)

    override suspend fun fetchPhotoList(username: String): List<Photo> {
        val prefix = "${username.lowercase()}/"
        val photoList =
            yandexS3Client
                .listObjectKeys(prefix = prefix)
                .map { key -> Photo(url = yandexS3Client.publicUrl(key = key)) }
        photoListCache = photoList
        return photoList
    }

    override suspend fun uploadPhoto(
        uri: String,
        username: String,
        width: Int,
        height: Int,
    ): Photo? {
        val data =
            compressPhoto(
                uri = uri,
                width = width,
                height = height,
            ) ?: return null
        val key = "${username.lowercase()}/${NSUUID().UUIDString}.jpg"
        val uploaded =
            yandexS3Client.putObject(
                key = key,
                data = data,
                contentType = "image/jpeg",
            )

        if (!uploaded) return null

        photoListCache = null
        return Photo(url = yandexS3Client.publicUrl(key = key))
    }

    override suspend fun deletePhoto(photoLink: String) {
        val key = yandexS3Client.extractKey(photoLink = photoLink) ?: return
        yandexS3Client.deleteObject(key = key)
        photoListCache = null
    }

    override fun clearCache() {
        photoListCache = null
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun readPhotoBytes(uri: String): ByteArray? {
        val data = readPhotoData(uri = uri) ?: return null
        return data.bytes?.readBytes(data.length.toInt())
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun compressPhoto(
        uri: String,
        width: Int,
        height: Int,
    ): ByteArray? {
        val image = loadUIImage(uri = uri) ?: return null

        UIGraphicsBeginImageContextWithOptions(
            CGSizeMake(width.toDouble(), height.toDouble()),
            true,
            1.0,
        )
        image.drawInRect(CGRectMake(0.0, 0.0, width.toDouble(), height.toDouble()))
        val resizedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()

        var quality = 1.0
        var data: NSData?
        do {
            data = resizedImage?.let { image -> UIImageJPEGRepresentation(image, quality) }
            quality -= JPEG_QUALITY_STEP
        } while (data != null && data.length > DEFAULT_BYTE_SIZE.toULong() && quality > MIN_JPEG_QUALITY)

        return data?.bytes?.readBytes(data.length.toInt())
    }

    private fun loadUIImage(uri: String): UIImage? = readPhotoData(uri = uri)?.let { data -> UIImage.imageWithData(data) }

    private fun readPhotoData(uri: String): NSData? =
        if (uri.startsWith("file://")) {
            NSURL.URLWithString(uri)?.let { url ->
                NSData.dataWithContentsOfURL(url)
            }
        } else {
            NSData.dataWithContentsOfFile(uri)
        }
}
