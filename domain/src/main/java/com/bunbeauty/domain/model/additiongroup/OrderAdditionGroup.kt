package com.bunbeauty.domain.model.additiongroup

data class OrderAdditionGroup(
    val uuid: String,
    val name: String,
    val priority: Int,
) {
    companion object {
        val mock =
            OrderAdditionGroup(
                uuid = "",
                name = "",
                priority = 0,
            )
    }
}
