package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.user.LoginUser

interface UserAuthorizationRepo {

    suspend fun login(username: String, password: String): LoginUser?
    fun updateNotificationToken()
    fun updateNotificationToken(notificationToken: String)
    suspend fun clearNotificationToken()
}
