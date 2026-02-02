package com.bunbeauty.data.repository

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.work.UpdateNotificationTokenWorker
import com.bunbeauty.domain.repo.DataStoreRepo

class AndroidUserAuthorizationRepository(
    networkConnector: FoodDeliveryApi,
    dataStoreRepo: DataStoreRepo,
    private val context: Context,
) : BaseUserAuthorizationRepository(
        networkConnector = networkConnector,
        dataStoreRepo = dataStoreRepo,
    ) {
    override fun updateNotificationToken() {
        val workRequest =
            OneTimeWorkRequestBuilder<UpdateNotificationTokenWorker>()
                .build()

        WorkManager
            .getInstance(context)
            .enqueue(workRequest)
    }

    override fun updateNotificationToken(notificationToken: String) {
        val data =
            Data
                .Builder()
                .putString(
                    UpdateNotificationTokenWorker.NOTIFICATION_TOKEN_KEY,
                    notificationToken,
                ).build()

        val workRequest =
            OneTimeWorkRequestBuilder<UpdateNotificationTokenWorker>()
                .setInputData(data)
                .build()

        WorkManager
            .getInstance(context)
            .enqueue(workRequest)
    }
}
