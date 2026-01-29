package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.settings.WorkType

interface CompanyRepo {
    suspend fun updateTypeWork(
        workInfoData: WorkType,
        companyUuid: String,
        token: String,
    )
}
