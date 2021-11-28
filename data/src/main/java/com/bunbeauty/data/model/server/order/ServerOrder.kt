package com.bunbeauty.data.model.server.order

import com.bunbeauty.data.model.server.ServerCartProduct
import kotlinx.serialization.Serializable

@Serializable
data class ServerOrder(
    var uuid: String = "",
    var cafeUuid: String = "",
    val cartProducts: List<ServerCartProduct> = emptyList(),
    val orderEntity: ServerOrderEntity = ServerOrderEntity(),
    val timestamp: Long = 0
)
