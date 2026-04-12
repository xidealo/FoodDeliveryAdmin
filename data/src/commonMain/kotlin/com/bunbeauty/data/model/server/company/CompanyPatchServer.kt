package com.bunbeauty.data.model.server.company

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class CompanyPatchServer(
    @SerialName("workType")
    val workType: String,
)
