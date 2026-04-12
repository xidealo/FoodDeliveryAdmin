package com.bunbeauty.data.mapper.company

import com.bunbeauty.data.model.server.company.CompanyPatchServer
import com.bunbeauty.domain.model.settings.WorkType

val mapWorkInfoToCompanyPatchServer: (WorkType) -> CompanyPatchServer =
    { workInfoData ->
        CompanyPatchServer(
            workType = workInfoData.name,
        )
    }
