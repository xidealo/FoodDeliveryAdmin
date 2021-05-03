package com.bunbeauty.domain.repository.cafe

import com.bunbeauty.data.model.Cafe
import kotlinx.coroutines.flow.Flow

interface CafeRepo {
    val cafeListFlow: Flow<List<Cafe>>
    suspend fun refreshCafeList()

}