package com.bunbeauty.data.websocket

import com.bunbeauty.data.model.server.order.OrderServer
import kotlinx.coroutines.flow.Flow

interface OrderUpdatesWebSocket {
    suspend fun getUpdatedOrderFlowByCafeUuid(
        token: String,
        cafeUuid: String,
    ): Flow<ApiResultForWebsocket<OrderServer>>

    suspend fun unsubscribe(message: String)
}
