package com.bunbeauty.domain.feature.order_list

import com.bunbeauty.domain.NotificationService
import javax.inject.Inject

class UnsubscribeFromCafeNotificationUseCase @Inject constructor(
    private val getSelectedCafe: GetSelectedCafeUseCase,
    private val notificationService: NotificationService,
) {

    suspend operator fun invoke() {
        val selectedCafe = getSelectedCafe() ?: return

        notificationService.unsubscribeFromNotifications(selectedCafe.uuid)
    }
}