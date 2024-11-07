package com.bunbeauty.domain.repo

interface UserAuthorizationRepo {

    suspend fun login(username: String, password: String): Triple<String, String, String>?
    suspend fun updateNotificationToken(newNotificationToken: String? = null)
}
