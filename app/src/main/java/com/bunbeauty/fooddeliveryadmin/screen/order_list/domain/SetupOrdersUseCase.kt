package com.bunbeauty.fooddeliveryadmin.screen.order_list.domain

import com.bunbeauty.data.repository.OrderRepository
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class SetupOrdersUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val orderRepository: OrderRepository,
) {

    suspend operator fun invoke() {
        val cafeUuid = dataStoreRepo.cafeUuid.firstOrNull() ?: return
        val token = dataStoreRepo.token.firstOrNull() ?: return
        orderRepository.loadOrderListByCafeUuid(token = token, cafeUuid = cafeUuid)
        orderRepository.subscribeOnOrderList(token = token, cafeUuid = cafeUuid)
    }
}
