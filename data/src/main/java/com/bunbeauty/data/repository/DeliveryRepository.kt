package com.bunbeauty.data.repository

import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.DeliveryRepo
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class DeliveryRepository @Inject constructor(
    private val apiRepo: ApiRepo,
    private val dataStoreRepo: DataStoreRepo
) : DeliveryRepo {

    override suspend fun refreshDelivery() {
        apiRepo.delivery.collect { delivery ->
            dataStoreRepo.saveDelivery(delivery)
        }
    }
}