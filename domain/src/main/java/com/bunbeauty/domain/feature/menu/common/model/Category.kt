package com.bunbeauty.domain.feature.menu.common.model

data class Category(
    val uuid: String,
    val name: String,
    val priority: Int
) {
    companion object {
        val mock = Category(
            uuid = "",
            name = "",
            priority = 0
        )
    }
}
