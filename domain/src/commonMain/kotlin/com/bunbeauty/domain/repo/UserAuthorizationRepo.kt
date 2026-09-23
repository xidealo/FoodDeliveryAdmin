package com.bunbeauty.domain.repo

import com.bunbeauty.domain.feature.login.SessionValidationResult
import com.bunbeauty.domain.model.user.LoginUser

interface UserAuthorizationRepo {
    suspend fun login(
        username: String,
        password: String,
    ): LoginUser?

    suspend fun validateSession(): SessionValidationResult

    fun updateNotificationToken()

    fun updateNotificationToken(notificationToken: String)

    suspend fun clearNotificationToken()
}
