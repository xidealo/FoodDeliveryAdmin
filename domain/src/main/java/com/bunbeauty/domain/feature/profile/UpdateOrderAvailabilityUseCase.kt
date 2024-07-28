package com.bunbeauty.domain.feature.profile

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class OrderAvailabilityNotUpdated : Exception()

class UpdateOrderAvailabilityUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val orderRepo: OrderRepo
) {

    suspend operator fun invoke(isAvailable: Boolean): Boolean {
        val token = dataStoreRepo.token.firstOrNull() ?: throw NoTokenException()
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        return orderRepo.updateOrderAvailability(
            token = token,
            isAvailable = isAvailable,
            companyUuid = companyUuid
        ) ?: throw OrderAvailabilityNotUpdated()
    }
}
