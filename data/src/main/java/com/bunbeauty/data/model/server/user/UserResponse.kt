package com.bunbeauty.data.model.server.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(

    @SerialName("uuid")
    val uuid: String,

    @SerialName("username")
    val username: String,

    @SerialName("role")
    val role: String,

    @SerialName("unlimitedNotification")
    val unlimitedNotification: Boolean
)
