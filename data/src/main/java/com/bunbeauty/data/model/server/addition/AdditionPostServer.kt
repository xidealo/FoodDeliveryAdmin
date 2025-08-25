package com.bunbeauty.data.model.server.addition

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdditionPostServer(
    @SerialName("name")
    val name: String?,
    @SerialName("photoLink")
    val photoLink: String,
    @SerialName("isVisible")
    val isVisible: Boolean,
    @SerialName("tag")
    val tag: String?,
    @SerialName("priority")
    val priority: Int?,
    @SerialName("fullName")
    val fullName: String?,
    @SerialName("price")
    val price: Int?
)
