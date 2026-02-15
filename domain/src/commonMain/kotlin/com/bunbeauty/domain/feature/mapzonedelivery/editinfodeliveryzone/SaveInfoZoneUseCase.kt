package com.bunbeauty.domain.feature.mapzonedelivery.editinfodeliveryzone

import com.bunbeauty.domain.exception.NoCafeException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.exception.ZoneNameException
import com.bunbeauty.domain.model.cafe.UpdateInfoDeliveryZone
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class SaveInfoZoneUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepo: CafeRepo,
) {
    suspend operator fun invoke(
        uuidZone: String,
        updateInfoZone: UpdateInfoDeliveryZone,
    ) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val cafeUuid = dataStoreRepo.cafeUuid.firstOrNull() ?: throw NoCafeException()
        when {
            updateInfoZone.name.isNullOrBlank() -> throw ZoneNameException()
        }
        cafeRepo.updateInfoDeliveryZone(
            cafeUuid = cafeUuid,
            zoneUuid = uuidZone,
            token = token,
            updateInfoZone = updateInfoZone,
        )
    }
}
