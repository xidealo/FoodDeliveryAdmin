package com.bunbeauty.domain.feature.mapzonedelivery.editinfodeliveryzone

import com.bunbeauty.domain.exception.NoCafeException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.cafe.DeliveryZone
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class GetZoneUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepo: CafeRepo,
) {
    suspend operator fun invoke(zoneUuid: String): DeliveryZone {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val cafeUuid = dataStoreRepo.cafeUuid.firstOrNull() ?: throw NoCafeException()
        return cafeRepo.getDeliveryZone(
            cafeUuid = cafeUuid,
            zoneUuid = zoneUuid,
            token = token,
        ) ?: throw Exception()
    }
}
