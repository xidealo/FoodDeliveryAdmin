package com.bunbeauty.data.model.server.order

import kotlinx.serialization.Serializable

@Serializable
data class ServerOrderEntity(
    val bonus: Int? = null,
    val email: String = "",
    val phone: String = "",
    val userId: String = ""
)
