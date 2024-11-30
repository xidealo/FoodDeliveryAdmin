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
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserAuthorizationRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi,
    private val dataStoreRepo: DataStoreRepo,
    @ApplicationContext private val context: Context
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
