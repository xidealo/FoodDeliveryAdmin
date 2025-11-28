package com.bunbeauty.domain.model.additiongroup

data class CreateAdditionGroup(
    val name: String,
    val priority: Int,
    val isVisible: Boolean,
    val singleChoice: Boolean
)
