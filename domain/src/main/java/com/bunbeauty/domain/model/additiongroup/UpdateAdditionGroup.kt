package com.bunbeauty.domain.model.additiongroup

data class UpdateAdditionGroup(
    val name: String? = null,
    val priority: Int? = null,
    val isVisible: Boolean? = null,
    val singleChoice: Boolean? = null
) {
    companion object {
        val mock = UpdateAdditionGroup(
            name = null,
            priority = null,
            singleChoice = null,
            isVisible = null
        )
    }
}
