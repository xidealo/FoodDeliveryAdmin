package com.bunbeauty.shared.di

import com.bunbeauty.domain.repo.UserAuthorizationRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object IosNotificationBridge : KoinComponent {
    private val userAuthorizationRepo: UserAuthorizationRepo by inject()

    fun refreshNotificationToken() {
        userAuthorizationRepo.updateNotificationToken()
    }

    fun updateNotificationToken(notificationToken: String) {
        userAuthorizationRepo
            .updateNotificationToken(notificationToken = notificationToken)
    }
}
