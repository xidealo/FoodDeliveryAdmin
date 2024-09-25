package com.bunbeauty.data.model.server.addition

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdditionServer(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("name")
    val name: String,
    @SerialName("priority")
    val priority: Int,
    @SerialName("fullName")
    val fullName: String?,
    @SerialName("price")
    val price: Int?,
    @SerialName("photoLink")
    val photoLink: String,
    @SerialName("isVisible")
    val isVisible: Boolean
)
