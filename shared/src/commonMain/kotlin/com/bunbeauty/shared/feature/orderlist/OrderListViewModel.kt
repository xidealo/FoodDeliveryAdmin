package com.bunbeauty.shared.feature.orderlist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.common.GetCafeUseCase
import com.bunbeauty.domain.feature.orderlist.ObserveOrderListStreamUseCase
import com.bunbeauty.domain.feature.orderlist.OrderListStreamState
import com.bunbeauty.domain.feature.orderlist.UnsubscribeOrderUpdatesUseCase
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.feature.orderlist.state.OrderList
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.takeWhile

private const val TAG = "OrderListViewModel"

private val reconnectDelaysMs = listOf(0L, 1_000L, 1_000L, 1_000L, 1_000L, 1_000L)

private enum class OrderListObservationResult {
    COMPLETED,
    ERROR,
}

class OrderListViewModel(
    private val observeOrderListStream: ObserveOrderListStreamUseCase,
    private val unsubscribeOrderUpdates: UnsubscribeOrderUpdatesUseCase,
    private val getCafeUseCase: GetCafeUseCase,
) : BaseStateViewModel<OrderList.DataState, OrderList.Action, OrderList.Event>(
        initState =
            OrderList.DataState(
                refreshing = false,
                hasConnectionError = false,
                loadingOrderUpdates = false,
                orderList = emptyList(),
                orderListState = OrderList.DataState.State.LOADING,
                cafe = null,
                loadingOrderList = false,
            ),
    ) {
    override fun reduce(
        action: OrderList.Action,
        dataState: OrderList.DataState,
    ) {
        when (action) {
            OrderList.Action.StartObserveOrders -> {
                stopObservingOrderList()
                observeOrderList()
            }

            OrderList.Action.StopObserveOrders -> stopObservingOrderList()
            OrderList.Action.RetryClick -> onRetryClicked()
            OrderList.Action.RefreshSwipe -> onRefresh()
            is OrderList.Action.OrderClick ->
                onOrderClicked(
                    orderUuid = action.orderUuid,
                    orderCode = action.orderCode,
                )
        }
    }

    private var orderListJob: Job? = null

    private fun onRefresh() {
        setState {
            copy(refreshing = true)
        }
        stopObservingOrderList()
        observeOrderList()
    }

    private fun onOrderClicked(
        orderUuid: String,
        orderCode: String,
    ) {
        sendEvent {
            OrderList.Event.OpenOrderDetailsEvent(
                orderUuid = orderUuid,
                orderCode = orderCode,
            )
        }
        sendEvent {
            OrderList.Event.CancelNotification(
                notificationId = orderCode.hashCode(),
            )
        }
    }

    private fun onRetryClicked() {
        stopObservingOrderList()
        observeOrderList()
    }

    private fun observeOrderList() {
        if (orderListJob != null) return

        setState {
            copy(
                hasConnectionError = false,
                loadingOrderList = true,
            )
        }

        orderListJob =
            viewModelScope.launchSafe(
                onError = {
                    setState {
                        copy(
                            refreshing = false,
                            hasConnectionError = true,
                            loadingOrderUpdates = false,
                            loadingOrderList = false,
                            orderListState = OrderList.DataState.State.SUCCESS,
                        )
                    }
                },
                block = {
                    val cafe = getCafeUseCase()

                    setState {
                        copy(
                            hasConnectionError = false,
                            orderListState = OrderList.DataState.State.SUCCESS,
                            cafe = cafe,
                        )
                    }

                    observeOrderListWithReconnect(cafeUuid = cafe.uuid)
                },
            )
    }

    private suspend fun observeOrderListWithReconnect(
        cafeUuid: String,
        reconnectAttemptIndex: Int = 0,
    ) {
        val observationResult = observeOrderListUntilError(cafeUuid = cafeUuid)
        if (observationResult != OrderListObservationResult.ERROR) return

        val shouldReconnect = reconnectAttemptIndex < reconnectDelaysMs.size
        unsubscribeOrderUpdates(
            message =
                if (shouldReconnect) {
                    "ws_reconnect_attempt_${reconnectAttemptIndex + 1}"
                } else {
                    "ws_reconnect_give_up"
                },
        )

        if (!shouldReconnect) return

        delay(reconnectDelaysMs[reconnectAttemptIndex])
        observeOrderListWithReconnect(
            cafeUuid = cafeUuid,
            reconnectAttemptIndex = reconnectAttemptIndex + 1,
        )
    }

    private suspend fun observeOrderListUntilError(cafeUuid: String): OrderListObservationResult {
        var observationResult = OrderListObservationResult.COMPLETED

        observeOrderListStream(cafeUuid)
            .onEach { streamState ->
                if (streamState is OrderListStreamState.Error) {
                    observationResult = OrderListObservationResult.ERROR
                    handleOrderListStreamError()
                }
            }.onCompletion {
                setState {
                    copy(
                        refreshing = false,
                        loadingOrderList = false,
                        loadingOrderUpdates = false,
                    )
                }
            }.takeWhile { streamState ->
                streamState !is OrderListStreamState.Error
            }.collect(::handleOrderListStreamState)

        return observationResult
    }

    private fun handleOrderListStreamState(streamState: OrderListStreamState) {
        when (streamState) {
            is OrderListStreamState.Loading -> {
                setState {
                    copy(loadingOrderUpdates = streamState.isLoading)
                }
            }

            is OrderListStreamState.Error -> Unit

            is OrderListStreamState.Orders -> {
                handleOrderListUpdated(orderList = streamState.list)
            }
        }
    }

    private fun handleOrderListStreamError() {
        setState {
            copy(
                refreshing = false,
                hasConnectionError = true,
                loadingOrderList = false,
                loadingOrderUpdates = false,
            )
        }
    }

    private fun handleOrderListUpdated(orderList: List<Order>) {
        val oldOrderList = mutableDataState.value.orderList
        val hasNewOrder = oldOrderList.size < orderList.size

        setState {
            copy(
                hasConnectionError = false,
                orderList = orderList,
                refreshing = false,
                loadingOrderList = false,
                orderListState = OrderList.DataState.State.SUCCESS,
            )
        }

        if (oldOrderList.isNotEmpty() && hasNewOrder) {
            sendEvent {
                OrderList.Event.ScrollToTop
            }
        }
    }

    private fun stopObservingOrderList() {
        orderListJob?.cancel()
        orderListJob = null

        viewModelScope.launchSafe(
            block = {
                unsubscribeOrderUpdates(message = "stopObservingOrderList")
            },
            onError = { error ->
                println("$TAG: unsubscribeOrderUpdates failed: ${error.message}")
            },
        )
    }
}
