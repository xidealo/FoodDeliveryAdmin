package com.bunbeauty.domain.feature.statistic

import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

// todo test
class GetCafeListByCityUuidUseCase @Inject constructor(
    private val cafeRepo: CafeRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(): List<Cafe> {
        val cityUuid = dataStoreRepo.managerCity.firstOrNull() ?: throw NoTokenException()
        return cafeRepo.getCafeListByCityUuid(cityUuid = cityUuid)
    }
}
