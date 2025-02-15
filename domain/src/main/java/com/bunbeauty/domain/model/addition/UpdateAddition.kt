package com.bunbeauty.domain.model.addition

data class UpdateAddition(
    val name: String? = null,
    val priority: Int? = null,
    val fullName: String? = null,
    val price: Int? = null,
    val photoLink: String? = null,
    val isVisible: Boolean? = null,
    val tag: String? = null
)
