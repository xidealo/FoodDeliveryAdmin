package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.UpdateCafe

interface CafeRepo {

    suspend fun getCafeByUuid(uuid: String): Cafe?
    suspend fun updateCafeFromTime(cafeUuid: String, fromDaySeconds: Int, token: String): Cafe?
    suspend fun updateCafeToTime(cafeUuid: String, toDaySeconds: Int, token: String): Cafe?
    suspend fun updateWorkCafe(
        updateCafe: UpdateCafe,
        cafeUuid: String,
        token: String
    )

    suspend fun patchCafe(
        cafeUuid: String,
        updateCafe: UpdateCafe,
        token: String
    )

    fun clearCache()
}
