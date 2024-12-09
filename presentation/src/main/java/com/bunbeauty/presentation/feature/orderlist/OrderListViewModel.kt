package com.bunbeauty.presentation.feature.orderlist

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.common.GetCafeListUseCase
import com.bunbeauty.domain.feature.orderlist.CheckIsAnotherCafeSelectedUseCase
import com.bunbeauty.domain.feature.orderlist.GetOrderErrorFlowUseCase
import com.bunbeauty.domain.feature.orderlist.GetOrderListFlowUseCase
import com.bunbeauty.domain.feature.orderlist.GetSelectedCafeUseCase
import com.bunbeauty.domain.feature.orderlist.SaveSelectedCafeUuidUseCase
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.orderlist.state.OrderList
import com.bunbeauty.presentation.feature.selectcafe.SelectableCafeItem
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

private const val TAG = "OrderListViewModel"

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val getOrderListFlow: GetOrderListFlowUseCase,
    private val getOrderErrorFlow: GetOrderErrorFlowUseCase,
    private val getSelectedCafe: GetSelectedCafeUseCase,
    private val getCafeList: GetCafeListUseCase,
    private val saveSelectedCafeUuid: SaveSelectedCafeUuidUseCase,
    private val checkIsAnotherCafeSelected: CheckIsAnotherCafeSelectedUseCase
) : BaseStateViewModel<OrderList.DataState, OrderList.Action, OrderList.Event>(
    initState = OrderList.DataState(
        refreshing = false,
        selectedCafe = null,
        hasConnectionError = false,
        orderList = emptyList(),
        orderListState = OrderList.DataState.State.LOADING,
        cafeList = emptyList(),
        showCafeList = false,
        loadingOrderList = false
    )
) {
    override fun reduce(action: OrderList.Action, dataState: OrderList.DataState) {
        when (action) {
            OrderList.Action.StartObserveOrders -> {
                setUpCafe()
                stopObservingOrderList()
                observeOrderList(currentOrderList = dataState.orderList)
            }

            OrderList.Action.StopObserveOrders -> stopObservingOrderList()
            OrderList.Action.RetryClick -> onRetryClicked(currentOrderList = dataState.orderList)
            OrderList.Action.RefreshSwipe -> onRefresh(currentOrderList = dataState.orderList)
            is OrderList.Action.OrderClick -> onOrderClicked(
                orderUuid = action.orderUuid,
                orderCode = action.orderCode
            )

            OrderList.Action.CafeClick -> onCafeClicked()
            OrderList.Action.CloseCafeListBottomSheet -> closeCafeListBottomSheet()
            is OrderList.Action.SelectedCafe -> onCafeSelected(action.cafeUuid)
        }
    }

    private var orderListJob: Job? = null
    private var orderErrorJob: Job? = null

    private fun onRefresh(currentOrderList: List<Order>) {
        setState {
            copy(refreshing = true)
        }
        stopObservingOrderList()
        observeOrderList(currentOrderList = currentOrderList)
    }

    private fun closeCafeListBottomSheet() {
        setState {
            copy(
                showCafeList = false
            )
        }
    }

    private fun onCafeClicked() {
        setState {
            copy(
                showCafeList = true
            )
        }
    }

    private fun onCafeSelected(cafeUuid: String) {
        viewModelScope.launchSafe(
            block = {
                if (checkIsAnotherCafeSelected(cafeUuid)) {
                    stopObservingOrderList()
                    saveSelectedCafeUuid(cafeUuid)
                    setUpCafe()
                    closeCafeListBottomSheet()
                }
            },
            onError = {
            }
        )
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

    private fun onRetryClicked(currentOrderList: List<Order>) {
        stopObservingOrderList()
        observeOrderList(currentOrderList = currentOrderList)
    }

    private fun setUpCafe() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(hasConnectionError = false)
                }
                val selectedCafe = getSelectedCafe()

                setState {
                    if (selectedCafe == null) {
                        copy(hasConnectionError = true)
                    } else {
                        copy(
                            hasConnectionError = false,
                            orderListState = OrderList.DataState.State.SUCCESS,
                            selectedCafe = selectedCafe,
                            cafeList = getCafeList().map { cafe ->
                                SelectableCafeItem(
                                    uuid = cafe.uuid,
                                    address = cafe.address,
                                    isSelected = cafe.uuid == selectedCafe.uuid
                                )
                            }
                        )
                    }
                }
            },
            onError = {
                setState {
                    copy(hasConnectionError = true)
                }
            }
        )
    }

    private fun observeOrderList(currentOrderList: List<Order>) {
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
                        loadingOrderList = false
                    )
                }
            },
            block = {
                val selectedCafe = getSelectedCafe() ?: return@launchSafe

                getOrderListFlow(selectedCafe.uuid).collect { orderList ->
                    setState {
                        copy(
                            orderList = orderList,
                            refreshing = false,
                            loadingOrderList = false
                        )
                    }

                    val hasNewOrder = currentOrderList.size < orderList.size

                    if (currentOrderList.isNotEmpty() && hasNewOrder) {
                        sendEvent {
                            OrderList.Event.ScrollToTop
                        }
                    }
                }
            }
        )

        orderErrorJob = viewModelScope.launchSafe(
            block = {
                getOrderErrorFlow().collect {
                    setState {
                        copy(hasConnectionError = true)
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
