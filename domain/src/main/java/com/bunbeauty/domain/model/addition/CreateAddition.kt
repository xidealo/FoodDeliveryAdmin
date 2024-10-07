package com.bunbeauty.domain.model.addition

data class CreateAddition(
    val name: String,
    val priority: Int,
    val fullName: String?,
    val price: Int?,
    val photoLink: String?,
    val isVisible: Boolean?
)