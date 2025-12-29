package com.bunbeauty.domain.model.cafe

data class DeliveryZone(
    val uuid: String,
    val deliveryZonePoint: List<DeliveryZonePoint>,
    val minOrderCost: Int?,
    val normalDeliveryCost: Int,
    val forLowDeliveryCost: Int?,
    val nameZone: String,
) {
    companion object {
        val mock =
            DeliveryZone(
                uuid = "123",
                deliveryZonePoint = emptyList(),
                minOrderCost = null,
                normalDeliveryCost = 100,
                forLowDeliveryCost = 150,
                nameZone = "Test_Zone",
            )
    }
}
