package com.bunbeauty.data.repository

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.model.server.request.UserAuthorizationRequest
import com.bunbeauty.data.work.UpdateNotificationTokenWorker
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.user.LoginUser
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo

class UserAuthorizationRepository(
    private val networkConnector: FoodDeliveryApi,
    private val dataStoreRepo: DataStoreRepo,
    private val context: Context
) : UserAuthorizationRepo {

    override suspend fun login(
        username: String,
        password: String
    ): LoginUser? {
        return when (
            val result = networkConnector.login(
                UserAuthorizationRequest(
                    username = username,
                    password = password
                )
            )
        ) {
            is ApiResult.Success -> {
                LoginUser(
                    token = result.data.token,
                    cafeUuid = result.data.cafeUuid,
                    companyUuid = result.data.companyUuid
                )
            }

            is ApiResult.Error -> null
        }
    }

    override fun updateNotificationToken() {
        val workRequest = OneTimeWorkRequestBuilder<UpdateNotificationTokenWorker>()
            .build()
        WorkManager.getInstance(context)
            .enqueue(workRequest)
    }

    override fun updateNotificationToken(notificationToken: String) {
        val data = Data.Builder()
            .putString(UpdateNotificationTokenWorker.NOTIFICATION_TOKEN_KEY, notificationToken)
            .build()
        val workRequest = OneTimeWorkRequestBuilder<UpdateNotificationTokenWorker>()
            .setInputData(inputData = data)
            .build()
        WorkManager.getInstance(context)
            .enqueue(workRequest)
    }

    override suspend fun clearNotificationToken() {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        networkConnector.deleteNotificationToken(token = token)
    }
}
