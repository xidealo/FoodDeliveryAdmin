package com.bunbeauty.data.model.server.addition

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderAdditionServer(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("name")
    val name: String,
    @SerialName("priority")
    val priority: Int,
)
