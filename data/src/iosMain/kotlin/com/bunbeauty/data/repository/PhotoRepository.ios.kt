package com.bunbeauty.data.repository

import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo

actual class PhotoRepository : PhotoRepo {
    actual override suspend fun getPhotoList(username: String): List<Photo> {
        TODO("Not yet implemented")
    }

    actual override suspend fun fetchPhotoList(username: String): List<Photo> {
        TODO("Not yet implemented")
    }

    actual override suspend fun uploadPhoto(
        uri: String,
        username: String,
        width: Int,
        height: Int,
    ): Photo? {
        TODO("Not yet implemented")
    }

    actual override suspend fun deletePhoto(photoLink: String) {
        TODO("Not yet implemented")
    }

    actual override fun clearCache() {
        TODO("Not yet implemented")
    }
}
