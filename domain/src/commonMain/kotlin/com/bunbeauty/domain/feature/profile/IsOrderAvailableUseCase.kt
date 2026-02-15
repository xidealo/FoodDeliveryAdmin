package com.bunbeauty.domain.feature.profile

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class IsOrderAvailableUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val orderRepo: OrderRepo,
) {
    suspend operator fun invoke(): Boolean {
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        return orderRepo.getOrderAvailability(companyUuid = companyUuid)?.isAvailable ?: true
    }
}
