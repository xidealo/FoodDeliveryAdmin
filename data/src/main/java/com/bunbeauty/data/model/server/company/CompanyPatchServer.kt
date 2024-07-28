package com.bunbeauty.data.model.server.company

import kotlinx.serialization.Serializable

@Serializable
class CompanyPatchServer(
    val isOpen: Boolean,
)