package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.Photo

interface PhotoRepo {
    suspend fun getPhotoList(username: String): List<Photo>

    suspend fun fetchPhotoList(username: String): List<Photo>

    suspend fun uploadPhoto(
        uri: String,
        username: String,
        width: Int,
        height: Int,
    ): Photo?

    suspend fun deletePhoto(photoLink: String)

    fun clearCache()
}
