package com.bunbeauty.data.model.server.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAuthorizationRequest(

    @SerialName("username")
    val username: String = "",

    @SerialName("password")
    val password: String = ""
)
