package com.bunbeauty.data.di

import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.model.user.LoginUser
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import com.bunbeauty.domain.repo.PhotoRepo

class IosStubUserAuthorizationRepository : UserAuthorizationRepo {
    override suspend fun login(
        username: String,
        password: String,
    ): LoginUser? = null

    override fun updateNotificationToken() = Unit

    override fun updateNotificationToken(notificationToken: String) = Unit

    override suspend fun clearNotificationToken() = Unit
}

class IosStubPhotoRepository : PhotoRepo {
    override suspend fun getPhotoList(username: String): List<Photo> = emptyList()

    override suspend fun fetchPhotoList(username: String): List<Photo> = emptyList()

    override suspend fun uploadPhoto(
        uri: String,
        username: String,
        width: Int,
        height: Int,
    ): Photo? = null

    override suspend fun deletePhoto(photoLink: String) = Unit

    override fun clearCache() = Unit
}
