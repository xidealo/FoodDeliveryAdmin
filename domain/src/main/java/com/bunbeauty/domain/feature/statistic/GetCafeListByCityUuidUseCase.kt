package com.bunbeauty.domain.feature.statistic

import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.first
import javax.inject.Inject

//todo test
class GetCafeListByCityUuidUseCase @Inject constructor(
   private val cafeRepo: CafeRepo,
   private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(): List<Cafe> {
        val cityUuid = dataStoreRepo.managerCity.first()
        return cafeRepo.getCafeListByCityUuid(cityUuid)
    }
}