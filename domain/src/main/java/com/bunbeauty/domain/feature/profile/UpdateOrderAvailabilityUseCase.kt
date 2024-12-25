package com.bunbeauty.domain.feature.profile

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class OrderAvailabilityNotUpdated : Exception()

class UpdateOrderAvailabilityUseCase (
    private val dataStoreRepo: DataStoreRepo,
    private val orderRepo: OrderRepo
) {

    suspend operator fun invoke(isAvailable: Boolean): Boolean {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        return orderRepo.updateOrderAvailability(
            token = token,
            isAvailable = isAvailable,
            companyUuid = companyUuid
        ) ?: throw OrderAvailabilityNotUpdated()
    }
}
