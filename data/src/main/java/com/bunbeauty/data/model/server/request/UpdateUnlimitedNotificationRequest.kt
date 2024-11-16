package com.bunbeauty.data.model.server.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class UpdateUnlimitedNotificationRequest(

    @SerialName("isEnabled")
    val isEnabled: Boolean
)
