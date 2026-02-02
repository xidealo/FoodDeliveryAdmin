package com.bunbeauty.data.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.domain.repo.DataStoreRepo
import com.google.firebase.messaging.FirebaseMessaging
import common.Constants.NOTIFICATION_TAG
import kotlinx.coroutines.tasks.await

class UpdateNotificationTokenWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val dataStoreRepo: DataStoreRepo,
    private val foodDeliveryApi: FoodDeliveryApi,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result =
        try {
            val notificationToken =
                inputData.getString(NOTIFICATION_TOKEN_KEY)
                    ?: getNotificationToken()

            UpdateNotificationTokenInteractor(
                dataStoreRepo = dataStoreRepo,
                foodDeliveryApi = foodDeliveryApi,
            ).update(notificationToken)

            Result.success()
        } catch (exception: Exception) {
            Log.e(NOTIFICATION_TAG, "UpdateNotificationTokenWorker: ${exception.message}")
            Result.failure()
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
