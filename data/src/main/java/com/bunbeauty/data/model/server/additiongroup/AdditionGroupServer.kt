package com.bunbeauty.data.model.server.additiongroup

import com.bunbeauty.data.model.server.addition.AdditionServer
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
    val isVisible: Boolean,
    @SerialName("additions")
    val additionServerList: List<AdditionServer>
)
