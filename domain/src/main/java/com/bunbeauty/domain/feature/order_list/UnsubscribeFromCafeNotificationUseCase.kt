package com.bunbeauty.domain.feature.order_list

import com.bunbeauty.domain.NotificationService
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class UnsubscribeFromCafeNotificationUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val notificationService: NotificationService
) {

    suspend operator fun invoke() {
        val cafeUuid = dataStoreRepo.cafeUuid.firstOrNull() ?: return

        notificationService.unsubscribeFromNotifications(cafeUuid)
    }
}