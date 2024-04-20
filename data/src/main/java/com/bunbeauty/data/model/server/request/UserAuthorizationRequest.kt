package com.bunbeauty.data.model.server.request

import kotlinx.serialization.Serializable

@Serializable
data class UserAuthorizationRequest(
    val username: String = "",
    val password: String = ""
)
