package com.bunbeauty.domain.feature.mapzonedelivery

import com.bunbeauty.domain.exception.NoCafeException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.cafe.DeliveryZonePoint
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class GetPolygonsDeliveryZoneUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepo: CafeRepo,
) {
    // add data class DeliveryZone
    suspend operator fun invoke(): List<List<DeliveryZonePoint>> {
        val cafeUuid = dataStoreRepo.cafeUuid.firstOrNull() ?: throw NoCafeException()
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val get = cafeRepo.getPositionDeliveryZone(cafeUuid = cafeUuid, token = token)
        return get
    }
}
