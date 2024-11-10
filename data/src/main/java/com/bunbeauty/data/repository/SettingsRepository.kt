package com.bunbeauty.data.repository

import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.SettingsRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

private const val IS_UNLIMITED_NOTIFICATION_DEFAULT = true

class SettingsRepository @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) : SettingsRepo {

    private var isUnlimitedNotificationCache: Boolean? = null

    override suspend fun init() {
        isUnlimitedNotificationCache =
            dataStoreRepo.isUnlimitedNotification.firstOrNull() ?: IS_UNLIMITED_NOTIFICATION_DEFAULT
    }

    override fun isUnlimitedNotification(): Boolean {
        return isUnlimitedNotificationCache ?: IS_UNLIMITED_NOTIFICATION_DEFAULT
    }

    override suspend fun updateUnlimitedNotification(isEnabled: Boolean) {
        dataStoreRepo.saveIsUnlimitedNotification(isUnlimitedNotification = isEnabled)
        isUnlimitedNotificationCache = isEnabled
    }
}
