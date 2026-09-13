package com.bunbeauty.domain.feature.order.usecase

import com.bunbeauty.domain.model.order.details.OrderAddress
import com.bunbeauty.domain.model.order.details.OrderDetails
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.CityRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import common.Constants.ADDRESS_DIVIDER
import kotlinx.coroutines.flow.firstOrNull

class GetOrderMapQueryUseCase(
    private val cafeRepo: CafeRepo,
    private val cityRepo: CityRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(orderDetails: OrderDetails): String? {
        if (orderDetails.isDelivery) {
            val addressBase = getAddressBase(orderDetails.address) ?: return null
            val cityName = getCityName(cafeUuid = orderDetails.cafeUuid)
            return if (cityName != null) {
                "$cityName$ADDRESS_DIVIDER$addressBase"
            } else {
                addressBase
            }
        } else {
            return null
        }
    }

    private fun getAddressBase(address: OrderAddress): String? {
        val description = address.description?.trim().orEmpty()
        if (description.isNotEmpty()) {
            return description
        }

        val street = address.street?.trim().orEmpty()
        val house = address.house?.trim().orEmpty()
        val parts =
            listOf(street, house).filter { part ->
                part.isNotEmpty()
            }
        return parts.joinToString(ADDRESS_DIVIDER).takeIf { query ->
            query.isNotEmpty()
        }
    }

    private suspend fun getCityName(cafeUuid: String): String? {
        val cafe = cafeRepo.getCafeByUuid(cafeUuid) ?: return null
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: return null
        return cityRepo
            .getCityByUuid(
                companyUuid = companyUuid,
                cityUuid = cafe.cityUuid,
            )?.name
            ?.trim()
            ?.takeIf { cityName ->
                cityName.isNotEmpty()
            }
    }
}
