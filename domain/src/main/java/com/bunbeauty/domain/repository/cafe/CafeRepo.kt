package com.bunbeauty.domain.repository.cafe

import com.bunbeauty.data.model.Cafe
import kotlinx.coroutines.flow.Flow

interface CafeRepo {
    val cafeList: Flow<List<Cafe>>
    fun getCafeById(id: String): Cafe?
    fun getCafeByIdFlow(id: String): Flow<Cafe?>
    suspend fun refreshCafeList()
}