package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.company.mapWorkInfoToCompanyPatchServer
import com.bunbeauty.domain.model.settings.WorkType
import com.bunbeauty.domain.repo.CompanyRepo

class CompanyRepository(
    private val networkConnector: FoodDeliveryApi,
) : CompanyRepo {
    private var cachedTypeWork: WorkType? = null

    override suspend fun updateTypeWork(
        workInfoData: WorkType,
        companyUuid: String,
        token: String,
    ) = when (
        val result =
            networkConnector.patchCompany(
                token = token,
                companyPatch = mapWorkInfoToCompanyPatchServer(workInfoData),
                companyUuid = companyUuid,
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
