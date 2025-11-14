package com.bunbeauty.data.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bunbeauty.common.Constants.NOTIFICATION_TAG
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.model.server.request.UpdateNotificationTokenRequest
import com.bunbeauty.domain.repo.DataStoreRepo
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class UpdateNotificationTokenWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val dataStoreRepo: DataStoreRepo,
    private val foodDeliveryApi: FoodDeliveryApi,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        try {
            val notificationToken = inputData.getString(NOTIFICATION_TOKEN_KEY) ?: getNotificationToken()
            Log.d(NOTIFICATION_TAG, "UpdateNotificationTokenWorker: token=$notificationToken")

            val token =
                dataStoreRepo.getToken() ?: run {
                    Log.e(NOTIFICATION_TAG, "UpdateNotificationTokenWorker: no token")
                    return Result.failure()
                }
            foodDeliveryApi.putNotificationToken(
                updateNotificationTokenRequest =
                    UpdateNotificationTokenRequest(
                        token = notificationToken,
                    ),
                token = token,
            )

            Log.d(NOTIFICATION_TAG, "UpdateNotificationTokenWorker: success")
            return Result.success()
        } catch (exception: Exception) {
            Log.e(NOTIFICATION_TAG, "UpdateNotificationTokenWorker: ${exception.message}")
            return Result.failure()
        }
    }

    private suspend fun getNotificationToken(): String =
        FirebaseMessaging
            .getInstance()
            .token
            .await()

    companion object {
        const val NOTIFICATION_TOKEN_KEY = "notification token"
    }
}
