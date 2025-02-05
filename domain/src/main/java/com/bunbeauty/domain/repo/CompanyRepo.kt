package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.settings.WorkInfo

interface CompanyRepo {

    suspend fun getTypeWork(companyUuid: String): WorkInfo?
    suspend fun updateTypeWork(
        workInfoData: WorkInfo,
        companyUuid: String,
        token: String
    )
}