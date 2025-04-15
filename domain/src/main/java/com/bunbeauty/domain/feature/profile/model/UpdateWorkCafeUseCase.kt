package com.bunbeauty.domain.feature.profile.model

import com.bunbeauty.domain.exception.NoCafeException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.cafe.UpdateCafe
import com.bunbeauty.domain.model.settings.WorkLoad
import com.bunbeauty.domain.model.settings.WorkType
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class UpdateWorkCafeUseCase(
    private val workLoadRepository: CafeRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(workLoad: WorkLoad, workInfoData: WorkType) {
        val cafeUuid = dataStoreRepo.cafeUuid.firstOrNull() ?: throw NoCafeException()
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        workLoadRepository.patchCafe(
            updateCafe = UpdateCafe(
                workload = workLoad,
                workType = workInfoData
            ),
            cafeUuid = cafeUuid,
            token = token
        )
    }
}
