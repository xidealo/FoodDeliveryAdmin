package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.company.mapWorkInfoServerToWorkInfo
import com.bunbeauty.data.mapper.company.mapWorkInfoToCompanyPatchServer
import com.bunbeauty.domain.model.settings.WorkInfo
import com.bunbeauty.domain.repo.CompanyRepo

class CompanyRepository(
    private val networkConnector: FoodDeliveryApi
) : CompanyRepo {

    private var cachedTypeWork: WorkInfo? = null

    override suspend fun getTypeWork(companyUuid: String): WorkInfo? {
        return when (val result = networkConnector.getWorkInfo(companyUuid = companyUuid)) {
            is ApiResult.Success -> {
                val workInfo =
                    result.data.mapWorkInfoServerToWorkInfo()
                cachedTypeWork = workInfo
                workInfo
            }

            is ApiResult.Error -> {
                null
            }
        }
    }

    override suspend fun updateTypeWork(
        workInfoData: WorkInfo,
        companyUuid: String,
        token: String
    ) {
        return when (
            val result = networkConnector.patchCompany(
                token = token,
                companyPatch = mapWorkInfoToCompanyPatchServer(workInfoData),
                companyUuid = companyUuid
            )
        ) {
            is ApiResult.Success -> {
                cachedTypeWork = workInfoData
            }

            is ApiResult.Error -> {
                throw result.apiError
            }
        }
    }
}
