package com.bunbeauty.fooddeliveryadmin.screen.order_list.domain

import com.bunbeauty.data.repository.CafeRepository
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetCafeListUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepository: CafeRepository
) {

    suspend operator fun invoke(): List<Cafe> {
        val cityUuid = dataStoreRepo.managerCity.firstOrNull() ?: return emptyList()
        return cafeRepository.getCafeListByCityUuid(cityUuid)
    }
}