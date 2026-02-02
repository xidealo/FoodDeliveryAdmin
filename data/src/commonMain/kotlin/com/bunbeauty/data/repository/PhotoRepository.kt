package com.bunbeauty.data.repository

import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.repo.PhotoRepo

expect class PhotoRepository : PhotoRepo {
    override suspend fun getPhotoList(username: String): List<Photo>

    override suspend fun fetchPhotoList(username: String): List<Photo>

    override suspend fun uploadPhoto(
        uri: String,
        username: String,
        width: Int,
        height: Int,
    ): Photo?

    override suspend fun deletePhoto(photoLink: String)

    override fun clearCache()
}
