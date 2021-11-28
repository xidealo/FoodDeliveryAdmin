package com.bunbeauty.data.model.server

import kotlinx.serialization.Serializable

@Serializable
data class ServerCartProduct(
    val count: Int = 0,
    val menuProduct: ServerMenuProduct = ServerMenuProduct()
)
