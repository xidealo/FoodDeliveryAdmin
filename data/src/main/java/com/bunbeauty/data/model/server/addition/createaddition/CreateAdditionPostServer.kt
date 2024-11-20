package com.bunbeauty.data.model.server.addition.createaddition

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateAdditionPostServer(
    @SerialName("name")
    val name: String?,
    @SerialName("priority")
    val priority: String?,
    @SerialName("fullName")
    val fullName: String?,
    @SerialName("price")
    val price: Int?,
    @SerialName("photoLink")
    val photoLink: String?,
    @SerialName("isVisible")
    val isVisible: Boolean?
)
