package com.bunbeauty.domain.feature.common

import com.bunbeauty.domain.exception.NoCafeException
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class GetCafeUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val cafeRepository: CafeRepo
) {

    suspend operator fun invoke(): Cafe {
        val cafeUuid = dataStoreRepo.cafeUuid.firstOrNull() ?: throw NoCafeException()

        return cafeRepository.getCafeByUuid(uuid = cafeUuid) ?: throw NoCafeException()
    }
}
