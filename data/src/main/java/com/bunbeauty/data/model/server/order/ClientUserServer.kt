package com.bunbeauty.data.model.server.order

import kotlinx.serialization.Serializable

@Serializable
data class ClientUserServer(
    val uuid: String = "",
    val phoneNumber: String = "",
    val email: String? = null,
)
