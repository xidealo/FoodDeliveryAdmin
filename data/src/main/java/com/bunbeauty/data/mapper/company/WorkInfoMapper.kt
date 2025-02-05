package com.bunbeauty.data.mapper.company

import com.bunbeauty.data.model.server.company.CompanyPatchServer
import com.bunbeauty.data.model.server.company.WorkInfoData
import com.bunbeauty.domain.model.settings.WorkInfo

val mapWorkInfoToCompanyPatchServer: (WorkInfo) -> CompanyPatchServer =
    { workInfoData ->
        CompanyPatchServer(
            workType = workInfoData.workType.name
        )
    }

val mapWorkInfoServerToWorkInfo: WorkInfoData.() -> WorkInfo =
    {
        WorkInfo(
            workType = WorkInfo.WorkType.valueOf(workType)
        )
    }
