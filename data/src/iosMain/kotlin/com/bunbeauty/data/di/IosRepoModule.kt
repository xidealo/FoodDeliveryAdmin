package com.bunbeauty.data.di

import cocoapods.FirebaseMessaging.FIRMessaging
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.model.server.request.UpdateNotificationTokenRequest
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.domain.model.Photo
import com.bunbeauty.domain.model.user.LoginUser
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.PhotoRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import common.ApiResult
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class IosUserAuthorizationRepository(
    private val networkConnector: FoodDeliveryApi,
    private val dataStoreRepo: DataStoreRepo,
) : UserAuthorizationRepo {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override suspend fun login(
        username: String,
        password: String,
    ): LoginUser? =
        when (
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

    override fun updateNotificationToken() {
        coroutineScope.launch {
            runCatching {
                updateNotificationTokenInternal(notificationToken = getNotificationToken())
            }
        }
    }

    override fun updateNotificationToken(notificationToken: String) {
        coroutineScope.launch {
            updateNotificationTokenInternal(notificationToken = notificationToken)
        }
    }

    override suspend fun clearNotificationToken() {
        val token = dataStoreRepo.getToken() ?: return
        networkConnector.deleteNotificationToken(token = token)
    }

    @OptIn(ExperimentalForeignApi::class)
    private suspend fun getNotificationToken(): String =
        suspendCoroutine { continuation ->
            FIRMessaging.messaging().tokenWithCompletion { token, error ->
                when {
                    error != null -> {
                        println("MY TOOKEN ERROR  FCM: $token")
                        continuation.resumeWithException(Exception(error.toString()))
                    }
                    token != null -> {
                        println("MY TOOKEN FCM: $token")

                        continuation.resume(token)
                    }
                    else -> continuation.resumeWithException(Exception("Token is null"))
                }
            }
        }

    private suspend fun updateNotificationTokenInternal(notificationToken: String) {
        val token = dataStoreRepo.getToken() ?: return
        networkConnector.putNotificationToken(
            updateNotificationTokenRequest =
                UpdateNotificationTokenRequest(
                    token = notificationToken,
                ),
            token = token,
        )
    }
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
