package com.bunbeauty.domain.feature.menu.common.model

data class UpdateCategory(
    val name: String,
    val priority: Int? = null,
) {
    companion object {
        val mock =
            UpdateCategory(
                name = "",
                priority = null,
            )
    }
}
