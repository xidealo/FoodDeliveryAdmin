package com.bunbeauty.data.model.server.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAuthorizationResponse(

    @SerialName("token")
    val token: String = "",

    @SerialName("cityUuid")
    val cityUuid: String = "",

    @SerialName("companyUuid")
    val companyUuid: String = ""
)
