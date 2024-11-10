package com.bunbeauty.data.model.server.addition.createaddition

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAdditionServer(
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