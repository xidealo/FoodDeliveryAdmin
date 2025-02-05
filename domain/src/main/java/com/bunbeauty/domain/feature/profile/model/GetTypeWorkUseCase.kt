package com.bunbeauty.domain.feature.profile.model

import com.bunbeauty.domain.exception.NoCompanyUuidException
import com.bunbeauty.domain.exception.NotFoundWorkInfoException
import com.bunbeauty.domain.model.settings.WorkInfo
import com.bunbeauty.domain.repo.CompanyRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class GetTypeWorkUseCase(
    private val workInfoRepository: CompanyRepo,
    private val dataStoreRepo: DataStoreRepo
) {
    suspend operator fun invoke(): WorkInfo {
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: throw NoCompanyUuidException()
        return workInfoRepository.getTypeWork(
            companyUuid = companyUuid
        ) ?: throw NotFoundWorkInfoException()
    }
}
