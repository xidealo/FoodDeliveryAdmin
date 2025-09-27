package com.bunbeauty.data.model.server.menproducttoadditiongroup

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MenuProductToAdditionGroupServer(
    @SerialName("menuProductUuid")
    val menuProductUuid: String,
    @SerialName("additionGroupUuid")
    val additionGroupUuid: String,
    @SerialName("uuid")
    val uuid: String
)
