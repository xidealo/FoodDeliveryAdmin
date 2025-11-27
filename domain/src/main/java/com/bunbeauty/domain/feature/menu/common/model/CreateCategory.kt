package com.bunbeauty.domain.feature.menu.common.model

data class CreateCategory(
    val name: String,
    val priority: Int,
) {
    companion object {
        val mock =
            CreateCategory(
                name = "",
                priority = 0,
            )
    }
}
