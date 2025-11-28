package com.bunbeauty.domain.model.additiongroup

data class AdditionGroup(
    val uuid: String,
    val name: String,
    val priority: Int,
    val isVisible: Boolean,
    val singleChoice: Boolean,
) {
    companion object {
        val mock =
            AdditionGroup(
                uuid = "",
                name = "",
                priority = 0,
                singleChoice = false,
                isVisible = false,
            )
    }
}
