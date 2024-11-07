package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.model.server.request.UpdateNotificationTokenRequest
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserAuthorizationRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi,
    private val dataStoreRepo: DataStoreRepo,
) : UserAuthorizationRepo {

    override suspend fun login(
        username: String,
        password: String
    ): Triple<String, String, String>? {
        return when (
            val result = networkConnector.login(
                UserAuthorizationRequest(
                    username = username,
                    password = password
                )
            )
        ) {
            is ApiResult.Success -> {
                Triple(
                    result.data.token,
                    result.data.cityUuid,
                    result.data.companyUuid
                )
            }

            is ApiResult.Error -> null
        }
    }

    override suspend fun updateNotificationToken() {
        val notificationToken = FirebaseMessaging.getInstance()
            .token
            .await()
        updateNotificationToken(notificationToken = notificationToken)
    }

    override suspend fun updateNotificationToken(notificationToken: String) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        networkConnector.putNotificationToken(
            updateNotificationTokenRequest = UpdateNotificationTokenRequest(
                token = notificationToken
            ),
            token = token
        )
    }

    override suspend fun clearNotificationToken() {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        networkConnector.deleteNotificationToken(token = token)
    }

}
