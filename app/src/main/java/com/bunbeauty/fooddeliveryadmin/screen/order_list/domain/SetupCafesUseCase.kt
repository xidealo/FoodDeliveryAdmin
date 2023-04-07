package com.bunbeauty.fooddeliveryadmin.screen.order_list.domain

import com.bunbeauty.data.repository.CafeRepository
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SetupCafesUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepository: CafeRepository,
) {

    suspend operator fun invoke() {
        cafeRepository.refreshCafeList(
            token = dataStoreRepo.token.first(),
            cityUuid = dataStoreRepo.managerCity.first()
        )
    }
}
