package com.bunbeauty.domain.use_case

import com.bunbeauty.domain.repo.CafeNotificationRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.repo.OrderRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val cafeNotificationRepository: CafeNotificationRepo,
    private val orderRepository: OrderRepo,
    private val menuProductRepo: MenuProductRepo,
) {

    suspend operator fun invoke() {
        dataStoreRepo.cafeUuid.firstOrNull()?.let { cafeUuid ->
            cafeNotificationRepository.unsubscribeFromCafeNotification(cafeUuid)
            orderRepository.unsubscribeOnOrderList("logout")
        }
        dataStoreRepo.clearCache()
        menuProductRepo.clearMenuProductList()
    }
}
