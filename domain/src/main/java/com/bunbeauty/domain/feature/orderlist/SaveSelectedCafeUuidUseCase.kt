package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.repo.DataStoreRepo

class SaveSelectedCafeUuidUseCase(
    private val getSelectedCafe: GetSelectedCafeUseCase,
    private val dataStoreRepo: DataStoreRepo
) {

    suspend operator fun invoke(cafeUuid: String) {
        getSelectedCafe()?.let { selectedCafe ->
            dataStoreRepo.savePreviousCafeUuid(selectedCafe.uuid)
        }

        dataStoreRepo.saveCafeUuid(cafeUuid)
    }
}
