package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.Constants.RELOAD_DELAY
import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.domain.model.Delivery
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.DeliveryRepo
import kotlinx.coroutines.delay
import javax.inject.Inject

class DeliveryRepository @Inject constructor(
    private val networkConnector: NetworkConnector,
    private val dataStoreRepo: DataStoreRepo
) : DeliveryRepo {

    override suspend fun refreshDelivery(token: String, companyUuid: String) {
        when (val result = networkConnector.getDelivery(token, companyUuid)) {
            is ApiResult.Success -> {
                result.data.let { delivery ->
                    dataStoreRepo.saveDelivery(
                        Delivery(
                            cost = delivery.cost,
                            forFree = delivery.forFree
                        )
                    )
                }
            }
            is ApiResult.Error -> {
                delay(RELOAD_DELAY)
                //refreshDelivery(token, cityUuid)
            }
        }
    }
}