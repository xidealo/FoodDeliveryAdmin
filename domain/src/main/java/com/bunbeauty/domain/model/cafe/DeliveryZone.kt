package com.bunbeauty.domain.model.cafe

data class DeliveryZone(
    val deliveryZonePoint: List<DeliveryZonePoint>,
    val minOrderCost: Int?,
    val normalDeliveryCost: Int,
    val forLowDeliveryCost: Int?,
    val nameZone: String,
)
