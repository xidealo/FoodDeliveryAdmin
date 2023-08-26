package com.bunbeauty.domain.feature.orderlist

import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetCafeListUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepository: CafeRepo,
) {

    suspend operator fun invoke(): List<Cafe> {
        val token = dataStoreRepo.token.firstOrNull() ?: return emptyList()
        val cityUuid = dataStoreRepo.managerCity.firstOrNull() ?: return emptyList()

        return cafeRepository.getCafeList(
            token = token,
            cityUuid = cityUuid,
        )
    }
}
