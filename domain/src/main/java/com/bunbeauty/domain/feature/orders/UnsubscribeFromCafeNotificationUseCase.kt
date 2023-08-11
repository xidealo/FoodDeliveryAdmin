package com.bunbeauty.domain.feature.orders

import com.bunbeauty.domain.NotificationService
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UnsubscribeFromCafeNotificationUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val notificationService: NotificationService,
) {

    suspend operator fun invoke() {
        val previousCafeUuid = dataStoreRepo.previousCafeUuid.firstOrNull() ?: return

        notificationService.unsubscribeFromNotifications(previousCafeUuid)
    }
}