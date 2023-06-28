package com.bunbeauty.domain.feature.order_list

import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class CheckIsAnotherCafeSelectedUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) {

    suspend operator fun invoke(cafeUuid: String?): Boolean {
        val currentCafeUuid = dataStoreRepo.cafeUuid.firstOrNull() ?: false
        return cafeUuid != currentCafeUuid
    }
}
