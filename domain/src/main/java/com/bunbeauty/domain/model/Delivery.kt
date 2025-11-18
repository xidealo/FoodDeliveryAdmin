package com.bunbeauty.domain.model

data class Delivery(
    val cost: Int = 0,
    val forFree: Int = 0,
) {
    companion object {
        val mock =
            Delivery(
                cost = 0,
                forFree = 0,
            )
    }
}
