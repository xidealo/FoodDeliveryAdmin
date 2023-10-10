package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.cafe.Cafe

interface CafeRepo {

    suspend fun getCafeByUuid(uuid: String): Cafe?
    suspend fun getCafeListByCityUuid(cityUuid: String): List<Cafe>
    suspend fun getCafeList(cityUuid: String): List<Cafe>
    suspend fun updateCafeFromTime(cafeUuid: String, fromDaySeconds: Int, token: String): Cafe?
    suspend fun updateCafeToTime(cafeUuid: String, toDaySeconds: Int, token: String): Cafe?
    fun clearCache()

}