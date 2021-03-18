package com.bunbeauty.domain.repository.cafe

import androidx.lifecycle.LiveData
import com.bunbeauty.data.model.Cafe

interface CafeRepo {
    val cafeListLiveData: LiveData<List<Cafe>>
    suspend fun refreshCafeList()

}