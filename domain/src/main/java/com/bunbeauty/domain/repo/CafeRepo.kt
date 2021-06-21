package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.cafe.Cafe
import kotlinx.coroutines.flow.Flow

interface CafeRepo {
    val cafeList: Flow<List<Cafe>>
    fun getCafeById(id: String): Cafe?
    fun getCafeByIdFlow(id: String): Flow<Cafe?>
    suspend fun refreshCafeList()
}