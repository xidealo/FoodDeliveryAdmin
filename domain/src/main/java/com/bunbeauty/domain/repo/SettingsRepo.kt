package com.bunbeauty.domain.repo

interface SettingsRepo {

    suspend fun isUnlimitedNotification(): Boolean
    suspend fun updateUnlimitedNotification(isEnabled: Boolean)
}
