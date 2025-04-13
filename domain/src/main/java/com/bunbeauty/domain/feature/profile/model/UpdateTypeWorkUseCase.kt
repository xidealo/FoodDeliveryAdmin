package com.bunbeauty.domain.feature.profile.model

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.settings.WorkType
import com.bunbeauty.domain.repo.CompanyRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class UpdateTypeWorkUseCase(
    private val workInfoRepository: CompanyRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(
        workInfoData: WorkType
    ) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        workInfoRepository.updateTypeWork(
            workInfoData = workInfoData,
            companyUuid = companyUuid,
            token = token
        )
    }
}
