package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.NotificationService
import javax.inject.Inject

class SubscribeToCafeNotificationUseCase @Inject constructor(
    private val notificationService: NotificationService
) {

    operator fun invoke(cafeUuid: String) {
        notificationService.subscribeOnNotifications(cafeUuid)
    }
}
