package com.bunbeauty.data.model.server.response

import com.bunbeauty.data.model.server.city.CityServer
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
    val unlimitedNotification: Boolean,

    @SerialName("city")
    val city: CityServer
)