package com.bunbeauty.data.model.server.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAuthorizationResponse(

    @SerialName("token")
    val token: String,

    @SerialName("cityUuid")
    val cafeUuid: String,

    @SerialName("companyUuid")
    val companyUuid: String
)
