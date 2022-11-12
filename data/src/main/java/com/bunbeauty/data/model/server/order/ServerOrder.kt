package com.bunbeauty.data.model.server.order
import kotlinx.serialization.Serializable

@Serializable
class ServerOrder(
    val uuid: String,
    val code: String,
    val status: String,
    val time: Long,
    val timeZone: String,
    val deferredTime: Long?
)