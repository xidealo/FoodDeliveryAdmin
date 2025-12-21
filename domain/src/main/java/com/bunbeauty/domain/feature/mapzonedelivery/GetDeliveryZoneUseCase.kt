package com.bunbeauty.domain.feature.mapzonedelivery

import com.bunbeauty.domain.exception.NoCafeException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.cafe.DeliveryZone
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class GetDeliveryZoneUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepo: CafeRepo,
) {
    suspend operator fun invoke(): List<DeliveryZone> {
        val cafeUuid = dataStoreRepo.cafeUuid.firstOrNull() ?: throw NoCafeException()
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val get = cafeRepo.getPositionDeliveryZone(cafeUuid = cafeUuid, token = token)
        return get
    }
}
