package com.bunbeauty.data.model.server.additiongroup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdditionGroupServer(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("name")
    val name: String,
    @SerialName("priority")
    val priority: Int,
    @SerialName("singleChoice")
    val singleChoice: Boolean,
    @SerialName("isVisible")
    val isVisible: Boolean
)
