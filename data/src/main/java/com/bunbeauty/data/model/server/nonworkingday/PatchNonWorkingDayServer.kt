package com.bunbeauty.data.model.server.nonworkingday

import kotlinx.serialization.Serializable

@Serializable
class PatchNonWorkingDayServer(
    val isVisible: Boolean,
)