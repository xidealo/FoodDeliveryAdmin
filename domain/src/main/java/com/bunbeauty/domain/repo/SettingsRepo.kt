package com.bunbeauty.domain.repo

interface SettingsRepo {

    suspend fun init()
    fun isUnlimitedNotification(): Boolean
    suspend fun updateUnlimitedNotification(isEnabled: Boolean)

}