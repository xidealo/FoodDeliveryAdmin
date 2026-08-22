package com.bunbeauty.data.model.server.clientuser

import kotlinx.serialization.Serializable

@Serializable
data class PostClientPushServer(
    val title: String,
    val body: String,
)
