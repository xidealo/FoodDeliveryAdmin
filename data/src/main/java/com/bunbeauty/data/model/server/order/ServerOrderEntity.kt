package com.bunbeauty.data.model.server.order

import kotlinx.serialization.Serializable

@Serializable
data class ServerOrderEntity(
    val address: ServerUserAddress? = null,
    val code: String = "",
    val bonus: Int? = null,
    val comment: String? = null,
    val deferred: String? = null,
    val delivery: Boolean = true,
    val email: String = "",
    val orderStatus: String = "NOT_ACCEPTED",
    val phone: String = "",
    val time: Long = 0,
    val userId: String = ""
)