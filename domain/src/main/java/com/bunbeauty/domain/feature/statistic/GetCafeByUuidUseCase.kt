package com.bunbeauty.domain.feature.statistic

import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.CafeRepo
import javax.inject.Inject

//todo test
class GetCafeByUuidUseCase @Inject constructor(
    private val cafeRepo: CafeRepo
) {
    suspend operator fun invoke(uuid: String): Cafe? {
        return cafeRepo.getCafeByUuid(uuid)
    }
}