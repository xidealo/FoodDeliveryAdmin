package com.bunbeauty.data.model.server.additiongroup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderAdditionGroupServer(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("name")
    val name: String,
    @SerialName("priority")
    val priority: Int,
)
