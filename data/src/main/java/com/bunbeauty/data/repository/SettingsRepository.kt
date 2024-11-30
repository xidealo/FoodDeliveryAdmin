package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.data.model.server.request.UpdateUnlimitedNotificationRequest
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.SettingsRepo
import javax.inject.Inject

private const val IS_UNLIMITED_NOTIFICATION_DEFAULT = true

class SettingsRepository @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val foodDeliveryApi: FoodDeliveryApi
) : SettingsRepo {

    private var isUnlimitedNotificationCache: Boolean? = null

    override suspend fun isUnlimitedNotification(): Boolean {
        return isUnlimitedNotificationCache
            ?: fetchUnlimitedNotification()
            ?: IS_UNLIMITED_NOTIFICATION_DEFAULT
    }

    override suspend fun updateUnlimitedNotification(isEnabled: Boolean) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        foodDeliveryApi.putUnlimitedNotification(
            updateUnlimitedNotificationRequest = UpdateUnlimitedNotificationRequest(
                isEnabled = isEnabled
            ),
            token = token
        )
        isUnlimitedNotificationCache = isEnabled
    }

    private suspend fun fetchUnlimitedNotification(): Boolean? {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val user = foodDeliveryApi.getUser(token = token).dataOrNull()
        isUnlimitedNotificationCache = user?.unlimitedNotification

        return user?.unlimitedNotification
    }
}
