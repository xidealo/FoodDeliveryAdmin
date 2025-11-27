package com.bunbeauty.data.model.server.cafe

import kotlinx.serialization.Serializable

@Serializable
data class PatchCafeServer(
    val fromTime: Int?,
    val toTime: Int?,
    val phone: String?,
    val latitude: Double?,
    val longitude: Double?,
    val address: String?,
    val isVisible: Boolean?,
    val additionalUtensils: Boolean?,
    val workload: String?,
    val workType: String?,
)
