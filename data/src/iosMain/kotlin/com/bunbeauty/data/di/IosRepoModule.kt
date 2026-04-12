package com.bunbeauty.data.di

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.model.user.LoginUser
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import com.bunbeauty.domain.repo.PhotoRepo
import common.ApiResult

class IosStubUserAuthorizationRepository(
    val networkConnector: FoodDeliveryApi
) : UserAuthorizationRepo {
    override suspend fun login(
        username: String,
        password: String,
    ): LoginUser? {
        return when (
            val result =
                networkConnector.login(
                    UserAuthorizationRequest(
                        username = username,
                        password = password,
                    ),
                )
        ) {
            is ApiResult.Success -> {
                LoginUser(
                    token = result.data.token,
                    cafeUuid = result.data.cafeUuid,
                    companyUuid = result.data.companyUuid,
                )
            }

            is ApiResult.Error -> null
        }
    }

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
