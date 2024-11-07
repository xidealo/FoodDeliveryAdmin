package com.bunbeauty.data.model.server.request

import kotlinx.serialization.Serializable

@Serializable
class UpdateNotificationTokenRequest(
    val token: String
)