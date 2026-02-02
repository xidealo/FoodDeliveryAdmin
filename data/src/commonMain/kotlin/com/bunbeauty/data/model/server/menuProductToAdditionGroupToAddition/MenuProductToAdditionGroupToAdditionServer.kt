package com.bunbeauty.data.model.server.menuProductToAdditionGroupToAddition

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuProductToAdditionGroupToAdditionServer(
    @SerialName("uuid")
    val uuid: String,
    @SerialName("isSelected")
    val isSelected: Boolean,
    @SerialName("isVisible")
    val isVisible: Boolean,
    @SerialName("menuProductToAdditionGroupUuid")
    val menuProductToAdditionGroupUuid: String,
    @SerialName("additionUuid")
    val additionUuid: String,
    @SerialName("priority")
    val priority: Int?,
)
