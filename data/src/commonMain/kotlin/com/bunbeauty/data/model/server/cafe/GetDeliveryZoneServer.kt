package com.bunbeauty.data.model.server.cafe

import kotlinx.serialization.Serializable

@Serializable
data class GetDeliveryZoneServer(
    val uuid: String,
    val name: String,
    val minOrderCost: Int?,
    val normalDeliveryCost: Int,
    val forLowDeliveryCost: Int?,
    val lowDeliveryCost: Int?,
    val isVisible: Boolean,
    val cafeUuid: String,
    val points: List<GetPointServer>,
)

@Serializable
data class GetDeliveryZoneResponse(
    val count: Int,
    val results: List<GetDeliveryZoneServer>,
)
