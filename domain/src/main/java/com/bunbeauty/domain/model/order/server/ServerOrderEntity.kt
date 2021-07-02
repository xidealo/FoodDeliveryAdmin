package com.bunbeauty.domain.model.order.server

data class ServerOrderEntity(
    val address: ServerUserAddress? = null,
    val code: String = "",
    val comment: String? = null,
    val deferred: String? = null,
    val delivery: Boolean = true,
    val email: String = "",
    val orderStatus: String = "",
    val phone: String = "",
    val time: Long = 0,
    val userId: String = ""
)