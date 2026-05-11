package com.bunbeauty.shared.feature.orderlist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.common.GetCafeUseCase
import com.bunbeauty.domain.feature.orderlist.ObserveOrderListStreamUseCase
import com.bunbeauty.domain.feature.orderlist.OrderListStreamState
import com.bunbeauty.domain.feature.orderlist.UnsubscribeOrderUpdatesUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.feature.orderlist.state.OrderList
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion

private const val TAG = "OrderListViewModel"

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

    init {
        stopObservingOrderList()
        observeOrderList()
    }

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
        setState {
            copy(
                hasConnectionError = false,
                loadingOrderList = true,
            )
        }

        if (orderListJob != null) return

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

                    observeOrderListStream(cafe.uuid)
                        .onCompletion {
                            setState {
                                copy(
                                    refreshing = false,
                                    loadingOrderList = false,
                                    loadingOrderUpdates = false,
                                )
                            }
                        }.collect { streamState ->
                            when (streamState) {
                                is OrderListStreamState.Loading -> {
                                    setState {
                                        copy(loadingOrderUpdates = streamState.isLoading)
                                    }
                                }

                                is OrderListStreamState.Error -> {
                                    setState {
                                        copy(
                                            refreshing = false,
                                            hasConnectionError = true,
                                            loadingOrderList = false,
                                            loadingOrderUpdates = false,
                                        )
                                    }
                                }

                                is OrderListStreamState.Orders -> {
                                    val oldOrderList = mutableDataState.value.orderList
                                    val hasNewOrder = oldOrderList.size < streamState.list.size

                                    setState {
                                        copy(
                                            orderList = streamState.list,
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
                            }
                        }
                },
            )
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
