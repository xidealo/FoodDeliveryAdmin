package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.cafe.Cafe
import kotlinx.coroutines.flow.Flow

interface CafeRepo {

    val cafeListFlow: Flow<List<Cafe>>

    fun getCafeByUuid(uuid: String): Flow<Cafe?>

    suspend fun getCafeList(): List<Cafe>

    suspend fun refreshCafeList()
}