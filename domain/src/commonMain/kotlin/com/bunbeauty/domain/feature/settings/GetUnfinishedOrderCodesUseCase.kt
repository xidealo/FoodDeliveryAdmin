package com.bunbeauty.domain.feature.settings

import com.bunbeauty.domain.enums.isFinal
import com.bunbeauty.domain.exception.updateaddition.NoCafeUuidException
import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class GetUnfinishedOrderCodesUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val orderRepo: OrderRepo,
) {
    suspend operator fun invoke(): List<String> {
        val token = dataStoreRepo.getToken() ?: return emptyList()
        val cafeUuid = dataStoreRepo.cafeUuid.firstOrNull() ?: throw NoCafeUuidException()

        return orderRepo
            .getOrderList(
                token = token,
                cafeUuid = cafeUuid,
            ).filter { order ->
                !order.orderStatus.isFinal()
            }.map { order ->
                order.code
            }
    }
}
