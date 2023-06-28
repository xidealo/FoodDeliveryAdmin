package com.bunbeauty.domain.feature.order_list

import com.bunbeauty.domain.repo.DataStoreRepo
import javax.inject.Inject

class SaveSelectedCafeUuidUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo
) {

    suspend operator fun invoke(cafeUuid: String) {
        dataStoreRepo.saveCafeUuid(cafeUuid)
    }

}