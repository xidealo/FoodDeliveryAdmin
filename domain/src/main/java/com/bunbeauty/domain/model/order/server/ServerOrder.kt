package com.bunbeauty.domain.model.order.server

import com.bunbeauty.domain.model.cart_product.ServerCartProduct

data class ServerOrder(
    var uuid: String = "",
    var cafeUuid: String = "",
    val cartProducts: List<ServerCartProduct> = emptyList(),
    val orderEntity: ServerOrderEntity = ServerOrderEntity(),
    val timestamp: Long = 0
)
