package com.bunbeauty.data.repository

import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo

actual class PhotoRepository : PhotoRepo {
    private var photoListCache: List<Photo>? = null

    actual override suspend fun getPhotoList(username: String): List<Photo> = photoListCache.orEmpty()

    actual override suspend fun fetchPhotoList(username: String): List<Photo> {
        photoListCache = emptyList()
        return emptyList()
    }

    actual override suspend fun uploadPhoto(
        uri: String,
        username: String,
        width: Int,
        height: Int,
    ): Photo? = throw NotImplementedError("Photo upload is not implemented on iOS yet")

    actual override suspend fun deletePhoto(photoLink: String) {
    }

    actual override fun clearCache() {
        photoListCache == null
    }
}
