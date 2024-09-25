package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.Photo

interface PhotoRepo {
    suspend fun getPhotoList(username: String): List<Photo>
    suspend fun fetchPhotoList(username: String): List<Photo>
    suspend fun uploadPhoto(uri: String, username: String): Photo?
    suspend fun deletePhoto(photoLink: String)
    suspend fun clearCache()
}
