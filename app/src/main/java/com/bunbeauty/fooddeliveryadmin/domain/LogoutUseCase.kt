package com.bunbeauty.fooddeliveryadmin.domain

import com.bunbeauty.data.repository.CafeNotificationRepository
import com.bunbeauty.data.repository.OrderRepository
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val cafeNotificationRepository: CafeNotificationRepository,
    private val orderRepository: OrderRepository,
) {

    suspend operator fun invoke() {
        dataStoreRepo.cafeUuid.firstOrNull()?.let { cafeUuid ->
            cafeNotificationRepository.unsubscribeFromCafeNotification(cafeUuid)
            orderRepository.unsubscribeOnOrderList("logout")
        }
        dataStoreRepo.clearCache()
    }
}