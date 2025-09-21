package com.bunbeauty.presentation.feature.orderlist

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.common.GetCafeUseCase
import com.bunbeauty.domain.feature.orderlist.GetOrderErrorFlowUseCase
import com.bunbeauty.domain.feature.orderlist.GetOrderListFlowUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.orderlist.state.OrderList
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import kotlinx.coroutines.Job

private const val TAG = "OrderListViewModel"

class OrderListViewModel(
    private val getOrderListFlow: GetOrderListFlowUseCase,
    private val getOrderErrorFlow: GetOrderErrorFlowUseCase,
    private val getCafeUseCase: GetCafeUseCase
) : BaseStateViewModel<OrderList.DataState, OrderList.Action, OrderList.Event>(
    initState = OrderList.DataState(
        refreshing = false,
        hasConnectionError = false,
        orderList = emptyList(),
        orderListState = OrderList.DataState.State.LOADING,
        cafe = null,
        loadingOrderList = false
    )
) {
    override fun reduce(action: OrderList.Action, dataState: OrderList.DataState) {
        when (action) {
            OrderList.Action.StartObserveOrders -> {
                stopObservingOrderList()
                observeOrderList()
            }

            OrderList.Action.StopObserveOrders -> stopObservingOrderList()
            OrderList.Action.RetryClick -> onRetryClicked()
            OrderList.Action.RefreshSwipe -> onRefresh()
            is OrderList.Action.OrderClick -> onOrderClicked(
                orderUuid = action.orderUuid,
                orderCode = action.orderCode
            )
        }
    }

    private var orderListJob: Job? = null
    private var orderErrorJob: Job? = null

    private fun onRefresh() {
        setState {
            copy(refreshing = true)
        }
        stopObservingOrderList()
        observeOrderList()
    }

    private fun onOrderClicked(orderUuid: String, orderCode: String) {
        sendEvent {
            OrderList.Event.OpenOrderDetailsEvent(
                orderUuid = orderUuid,
                orderCode = orderCode
            )
        }
        sendEvent {
            OrderList.Event.CancelNotification(
                notificationId = orderCode.hashCode()
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
                loadingOrderList = true
            )
        }

        if (orderListJob != null) return

        orderListJob = viewModelScope.launchSafe(
            onError = {
                setState {
                    copy(
                        refreshing = false,
                        hasConnectionError = true,
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
                        cafe = cafe
                    )
                }

                getOrderListFlow(cafe.uuid).collect { orderList ->
                    val oldOrderList = mutableDataState.value.orderList

                    val hasNewOrder = oldOrderList.size < orderList.size

                    setState {
                        copy(
                            orderList = orderList,
                            refreshing = false,
                            loadingOrderList = false,
                            orderListState = OrderList.DataState.State.SUCCESS
                        )
                    }

                    if (oldOrderList.isNotEmpty() && hasNewOrder) {
                        sendEvent {
                            OrderList.Event.ScrollToTop
                        }
                    }
                }
            }
        )

        orderErrorJob = viewModelScope.launchSafe(
            block = {
                getOrderErrorFlow(cafeUuid = getCafeUseCase().uuid).collect {
                    setState {
                        copy(
                            hasConnectionError = true,
                        )
                    }
                }
            },
            onError = { error ->
                Log.d(TAG, "getOrderErrorFlow: ${error.stackTrace}")
            }
        )
    }

    private fun stopObservingOrderList() {
        orderListJob?.cancel()
        orderErrorJob?.cancel()

        orderListJob = null
        orderErrorJob = null
    }
}
