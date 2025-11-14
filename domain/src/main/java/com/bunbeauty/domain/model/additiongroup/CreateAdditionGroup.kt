package com.bunbeauty.domain.model.additiongroup

data class CreateAdditionGroup(
    val name: String,
    val singleChoice: Boolean,
    val isVisible: Boolean,
    val priority: Int,
) {
    companion object {
        val mock =
            CreateAdditionGroup(
                name = "",
                singleChoice = false,
                priority = 0,
                isVisible = false,
            )
    }
}
