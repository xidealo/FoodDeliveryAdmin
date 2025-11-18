package com.bunbeauty.domain.model.order.details

data class OrderAddress(
    val description: String?,
    val street: String?,
    val house: String?,
    val flat: String?,
    val entrance: String?,
    val floor: String?,
    val comment: String?,
) {
    companion object {
        val mock =
            OrderAddress(
                description = null,
                street = null,
                house = null,
                flat = null,
                entrance = null,
                floor = null,
                comment = null,
            )
    }
}
