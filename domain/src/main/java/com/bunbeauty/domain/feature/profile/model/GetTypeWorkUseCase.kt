package com.bunbeauty.domain.feature.profile.model

import com.bunbeauty.domain.exception.NotFoundWorkInfoException
import com.bunbeauty.domain.exception.updateaddition.NoCafeUuidException
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class GetTypeWorkUseCase(
    private val workInfoRepository: CafeRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(): Cafe {
        val cafeUuid = dataStoreRepo.cafeUuid.firstOrNull() ?: throw NoCafeUuidException()
        return workInfoRepository.getCafeByUuid(
            uuid = cafeUuid
        ) ?: throw NotFoundWorkInfoException()
    }
}
