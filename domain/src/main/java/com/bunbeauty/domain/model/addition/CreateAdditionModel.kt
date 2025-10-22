package com.bunbeauty.domain.model.addition

data class CreateAdditionModel(
    val name: String,
    val isVisible: Boolean,
    val photoLink: String,
    val price: Int?,
    val fullName: String?,
    val priority: Int?,
    val tag: String?
)
