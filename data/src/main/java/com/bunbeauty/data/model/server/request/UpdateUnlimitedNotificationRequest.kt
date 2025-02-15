package com.bunbeauty.data.model.server.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUnlimitedNotificationRequest(

    @SerialName("isEnabled")
    val isEnabled: Boolean
)
