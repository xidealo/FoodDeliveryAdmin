package com.bunbeauty.domain.model.additiongroup

data class UpdateAdditionGroup(
    val name: String,
    val priority: Int? = null,
    val isVisible: Boolean? = null,
    val singleChoice: Boolean? = null
)
