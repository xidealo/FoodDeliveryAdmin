package com.bunbeauty.domain.feature.menu.common.model

data class UpdateCategory(
    val name: String,
    val priority: Int
) {
    companion object {
        val mock = UpdateCategory(
            name = "",
            priority = 0
        )
    }
}
