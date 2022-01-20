package com.bunbeauty.data.model.server.response

import kotlinx.serialization.Serializable

@Serializable
data class UserAuthorizationResponse(
    val token: String = "",
    val cityUuid: String = "",
    val companyUuid : String = ""
)
