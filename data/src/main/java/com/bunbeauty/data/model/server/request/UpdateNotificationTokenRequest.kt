package com.bunbeauty.data.model.server.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UpdateNotificationTokenRequest(

    @SerialName("token")
    val token: String
)
