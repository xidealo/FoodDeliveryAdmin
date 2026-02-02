package com.bunbeauty.data.model.server.cafe

import kotlinx.serialization.Serializable

@Serializable
data class PatchDeliveryZone(
    val name: String?,
    val minOrderCost: Int?,
    val normalDeliveryCost: Int?,
    val forLowDeliveryCost: Int?,
    val lowDeliveryCost: Int?,
    val isVisible: Boolean?,
    val cafeUuid: String?,
)
