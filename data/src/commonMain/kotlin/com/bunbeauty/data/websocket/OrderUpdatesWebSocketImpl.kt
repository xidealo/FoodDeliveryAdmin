package com.bunbeauty.data.websocket

import com.bunbeauty.data.model.server.order.OrderServer
import common.ApiError
import common.Constants.WEB_SOCKET_TAG
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readReason
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json

class OrderUpdatesWebSocketImpl(
    private val client: HttpClient,
    private val json: Json,
) : OrderUpdatesWebSocket {
    private data class ConnectionSnapshot(
        val session: DefaultClientWebSocketSession?,
        val webSocketJob: Job?,
        val scopeJob: Job?,
    )

    private var webSocketSession: DefaultClientWebSocketSession? = null

    private val mutableUpdatedOrderFlow =
        MutableSharedFlow<ApiResultForWebsocket<OrderServer>>(
            replay = 1,
            extraBufferCapacity = 64,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )

    private val mutex = Mutex()

    private var isSubscriptionInProgress = false
    private var subscribedCafeUuid: String? = null
    private var subscribedToken: String? = null

    private var webSocketJob: Job? = null
    private var scope: CoroutineScope? = null
    private var scopeJob: Job? = null

    override suspend fun getUpdatedOrderFlowByCafeUuid(
        token: String,
        cafeUuid: String,
    ): Flow<ApiResultForWebsocket<OrderServer>> {
        val toCloseAndCancel =
            mutex.withLock {
                val shouldResubscribe =
                    !isSubscriptionInProgress ||
                        subscribedCafeUuid != cafeUuid ||
                        subscribedToken != token

                if (shouldResubscribe) {
                    subscribeOnOrderUpdatesLocked(
                        token = token,
                        cafeUuid = cafeUuid,
                    )
                } else {
                    null
                }
            }

        toCloseAndCancel?.let { connection ->
            connection.session
                ?.runCatching {
                    close(
                        CloseReason(
                            CloseReason.Codes.NORMAL,
                            "reconnect",
                        ),
                    )
                }
            connection.webSocketJob?.cancelAndJoin()
            connection.scopeJob?.cancelAndJoin()
        }
        return mutableUpdatedOrderFlow.asSharedFlow()
    }

    private fun subscribeOnOrderUpdatesLocked(
        token: String,
        cafeUuid: String,
    ): ConnectionSnapshot {
        val previousSession = webSocketSession
        val previousJob = webSocketJob
        val previousScopeJob = scopeJob

        webSocketSession = null
        webSocketJob = null
        scope = null
        scopeJob = null
        isSubscriptionInProgress = true
        subscribedCafeUuid = cafeUuid
        subscribedToken = token

        val newScopeJob = SupervisorJob()
        val newScope = CoroutineScope(newScopeJob + Default)
        scopeJob = newScopeJob
        scope = newScope

        webSocketJob =
            newScope.launch {
                try {
                    mutableUpdatedOrderFlow.emit(ApiResultForWebsocket.Loading(isLoading = true))
                    client.wss(
                        HttpMethod.Get,
                        path = "user/order/subscribe",
                        port = 443,
                        request = {
                            header("Authorization", "Bearer $token")
                            parameter("cafeUuid", cafeUuid)
                        },
                    ) {
                        println("$WEB_SOCKET_TAG: WebSocket connected")
                        mutableUpdatedOrderFlow.emit(ApiResultForWebsocket.Loading(isLoading = false))
                        mutex.withLock {
                            webSocketSession = this
                        }
                        while (isActive) {
                            when (val frame = incoming.receive()) {
                                is Frame.Text -> {
                                    val text = frame.readText()
                                    println("$WEB_SOCKET_TAG: Frame.Text (${text.length} chars): $text")
                                    val serverModel =
                                        json.decodeFromString(
                                            deserializer = OrderServer.serializer(),
                                            string = text,
                                        )
                                    mutableUpdatedOrderFlow.tryEmit(ApiResultForWebsocket.Success(serverModel))
                                }

                                is Frame.Binary -> {
                                    println(
                                        "$WEB_SOCKET_TAG: Frame.Binary (${frame.data.size} bytes): ${bytesPreview(frame.data)}",
                                    )
                                }

                                is Frame.Ping -> send(Frame.Pong(frame.data))

                                is Frame.Pong -> {
                                    println(
                                        "$WEB_SOCKET_TAG: Frame.Pong (${frame.data.size} bytes): ${bytesPreview(frame.data)}",
                                    )
                                }

                                is Frame.Close -> {
                                    val reason = frame.readReason()
                                    println("$WEB_SOCKET_TAG: Frame.Close reason=$reason")
                                    break
                                }
                            }
                        }
                    }
                } catch (exception: WebSocketException) {
                    logWsException("WebSocketException", exception)
                    mutableUpdatedOrderFlow.emit(
                        ApiResultForWebsocket.Error(ApiError(message = describeException(exception))),
                    )
                } catch (exception: ClosedReceiveChannelException) {
                    logWsException("ClosedReceiveChannelException", exception)
                } catch (exception: Exception) {
                    logWsException("Exception", exception)
                    mutableUpdatedOrderFlow.emit(
                        ApiResultForWebsocket.Error(ApiError(message = describeException(exception))),
                    )
                } finally {
                    mutableUpdatedOrderFlow.emit(ApiResultForWebsocket.Loading(isLoading = false))
                    mutex.withLock {
                        isSubscriptionInProgress = false
                        webSocketSession = null
                        subscribedCafeUuid = null
                        subscribedToken = null
                        scope = null
                        scopeJob = null
                    }
                }
            }
        return ConnectionSnapshot(
            session = previousSession,
            webSocketJob = previousJob,
            scopeJob = previousScopeJob,
        )
    }

    override suspend fun unsubscribe(message: String) {
        val snapshot =
            mutex.withLock {
                val snapshotSession = webSocketSession
                val snapshotJob = webSocketJob
                val snapshotScopeJob = scopeJob
                webSocketSession = null
                webSocketJob = null
                isSubscriptionInProgress = false
                subscribedCafeUuid = null
                subscribedToken = null
                scope = null
                scopeJob = null
                ConnectionSnapshot(
                    session = snapshotSession,
                    webSocketJob = snapshotJob,
                    scopeJob = snapshotScopeJob,
                )
            }

        snapshot.session
            ?.runCatching {
                close(CloseReason(CloseReason.Codes.NORMAL, message))
            }
        println("$WEB_SOCKET_TAG: webSocketSession closed ($message)")

        snapshot.webSocketJob?.cancelAndJoin()
        snapshot.scopeJob?.cancelAndJoin()
    }

    private fun logWsException(
        label: String,
        throwable: Throwable,
    ) {
        println(
            "$WEB_SOCKET_TAG: $label -> ${describeException(throwable)}\n" +
                throwable.stackTraceToString(),
        )
    }

    private fun describeException(throwable: Throwable): String {
        val parts = mutableListOf<String>()
        var current: Throwable? = throwable
        var depth = 0
        while (current != null && depth < 5) {
            val className = current::class.simpleName ?: "Throwable"
            val msg = current.message ?: "<no message>"
            parts += if (depth == 0) "$className: $msg" else "caused by $className: $msg"
            current = current.cause?.takeIf { it !== current }
            depth++
        }
        return parts.joinToString(" | ")
    }

    private fun bytesPreview(
        bytes: ByteArray,
        maxBytes: Int = 64,
    ): String {
        if (bytes.isEmpty()) return "<empty>"
        val preview = bytes.take(maxBytes).toByteArray()

        val hex =
            preview.joinToString(separator = " ") { b ->
                (b.toInt() and 0xFF).toString(16).padStart(2, '0')
            }

        val utf8 =
            runCatching { preview.decodeToString() }
                .getOrNull()
                ?.replace("\n", "\\n")
                ?.replace("\r", "\\r")
                ?.replace("\t", "\\t")

        return buildString {
            append("hex=[")
            append(hex)
            if (bytes.size > maxBytes) append(" …")
            append("]")
            if (!utf8.isNullOrBlank()) {
                append(" utf8=\"")
                append(utf8)
                append("\"")
            }
        }
    }
}
