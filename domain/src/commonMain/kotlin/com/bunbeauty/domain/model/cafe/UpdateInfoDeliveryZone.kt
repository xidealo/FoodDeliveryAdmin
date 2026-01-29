package com.bunbeauty.domain.model.cafe

data class UpdateInfoDeliveryZone(
    val name: String? = null,
    val minOrderCost: Int? = null,
    val normalDeliveryCost: Int? = null,
    val forLowDeliveryCost: Int? = null,
    val lowDeliveryCost: Int? = null,
) {
    companion object {
        val mock =
            UpdateInfoDeliveryZone(
                name = "Кимры",
                minOrderCost = 12,
                normalDeliveryCost = 100,
                forLowDeliveryCost = 150,
                lowDeliveryCost = 1000,
            )
    }
}
