package com.bunbeauty.data.model.server.order

import kotlinx.serialization.Serializable

@Serializable
class ClientUserServer(
    val uuid: String,
    val phoneNumber: String,
    val email: String?,
    val isProblematic: Boolean = false,
)
