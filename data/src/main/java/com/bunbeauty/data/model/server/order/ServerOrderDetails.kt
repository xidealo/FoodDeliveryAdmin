package com.bunbeauty.data.model.server.order

import com.bunbeauty.data.model.server.CartProductServer
import kotlinx.serialization.Serializable

@Serializable
class ServerOrderDetails(
    val uuid: String,
    val code: String,
    val status: String,
    val time: Long,
    val timeZone: String,
    val isDelivery: Boolean,
    val deferredTime: Long?,
    val addressDescription: String,
    val comment: String?,
    val clientUser: ClientUserServer?,
    val cafeUuid: String,
    val deliveryCost: Int?,
    val oderProductList: List<CartProductServer>,
    val availableStatusList: List<String>
)
