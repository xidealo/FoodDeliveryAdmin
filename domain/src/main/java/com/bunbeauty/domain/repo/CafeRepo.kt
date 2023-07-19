package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.cafe.Cafe

interface CafeRepo {

    suspend fun getCafeByUuid(uuid: String): Cafe?
    suspend fun getCafeListByCityUuid(cityUuid: String): List<Cafe>
    suspend fun getCafeList(token: String, cityUuid: String): List<Cafe>
    fun clearCache()

}