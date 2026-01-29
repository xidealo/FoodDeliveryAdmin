package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.DeliveryZone
import com.bunbeauty.domain.model.cafe.UpdateCafe
import com.bunbeauty.domain.model.cafe.UpdateInfoDeliveryZone

interface CafeRepo {
    suspend fun getCafeByUuid(uuid: String): Cafe?

    suspend fun updateCafeFromTime(
        cafeUuid: String,
        fromDaySeconds: Int,
        token: String,
    ): Cafe?

    suspend fun updateCafeToTime(
        cafeUuid: String,
        toDaySeconds: Int,
        token: String,
    ): Cafe?

    suspend fun patchCafe(
        cafeUuid: String,
        updateCafe: UpdateCafe,
        token: String,
    )

    suspend fun getPositionDeliveryZone(
        cafeUuid: String,
        token: String,
    ): List<DeliveryZone>

    suspend fun getDeliveryZone(
        cafeUuid: String,
        zoneUuid: String,
        token: String,
    ): DeliveryZone?

    suspend fun updateInfoDeliveryZone(
        cafeUuid: String,
        zoneUuid: String,
        token: String,
        updateInfoZone: UpdateInfoDeliveryZone,
    )

    fun clearCache()
}
