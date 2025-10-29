package com.bunbeauty.domain.model.addition

data class OrderAddition(
    val uuid: String,
    val name: String,
    val priority: Int
) {
    companion object {
        val mock = OrderAddition(
            uuid = "",
            name = "",
            priority = 0
        )
    }
}
