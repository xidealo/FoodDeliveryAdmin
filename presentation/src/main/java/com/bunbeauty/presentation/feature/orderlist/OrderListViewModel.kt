package com.bunbeauty.presentation.feature.orderlist

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

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val getOrderListFlow: GetOrderListFlowUseCase,
    private val getOrderErrorFlow: GetOrderErrorFlowUseCase,
    private val getSelectedCafe: GetSelectedCafeUseCase,
    private val getCafeList: GetCafeListUseCase,
    private val saveSelectedCafeUuid: SaveSelectedCafeUuidUseCase,
    private val checkIsAnotherCafeSelected: CheckIsAnotherCafeSelectedUseCase,
) : BaseStateViewModel<OrderList.DataState, OrderList.Action, OrderList.Event>(
    initState = OrderList.DataState(
        refreshing = false,
        selectedCafe = null,
        cafeState = OrderList.DataState.State.LOADING,
        orderList = emptyList(),
        orderListState = OrderList.DataState.State.LOADING,
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

    private fun onCafeClicked() {
        viewModelScope.launchSafe(
            block = {
                val cafeList = getCafeList().map { cafe ->
                    SelectableCafeItem(
                        uuid = cafe.uuid,
                        address = cafe.address,
                        isSelected = cafe.uuid == mutableDataState.value.selectedCafe?.uuid
                    )
                }

                //TODO set state for BS
            },
            onError = {
                // No idea how to handle this
            },
        )
    }

    fun onCafeSelected(cafeUuid: String) {
        viewModelScope.launchSafe(
            block = {
                if (checkIsAnotherCafeSelected(cafeUuid)) {
                    stopObservingOrderList()
                    saveSelectedCafeUuid(cafeUuid)
                    setUpCafe()
                }
            },
            onError = {
                // No idea how to handle this
            },
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
                    copy(cafeState = OrderList.DataState.State.LOADING)
                }
                val selectedCafe = getSelectedCafe()

                setState {
                    if (selectedCafe == null) {
                        copy(cafeState = OrderList.DataState.State.ERROR)
                    } else {
                        copy(
                            cafeState = OrderList.DataState.State.SUCCESS,
                            selectedCafe = selectedCafe
                        )
                    }
                }
            },
            onError = {
                setState {
                    copy(cafeState = OrderList.DataState.State.ERROR)
                }
            },
        )
    }

    private fun observeOrderList(currentOrderList: List<Order>) {

        setState {
            copy(orderListState = OrderList.DataState.State.SUCCESS)
        }

        if (orderListJob != null) return

        orderListJob = viewModelScope.launchSafe(
            onError = {
                setState {
                    copy(
                        refreshing = false,
                        orderListState = OrderList.DataState.State.ERROR
                    )
                }
            },
            block = {
                //TODO (add message that cafe is not selected)
                val selectedCafe = getSelectedCafe() ?: return@launchSafe

                getOrderListFlow(selectedCafe.uuid).collect { orderList ->
                    setState {
                        copy(
                            orderList = orderList,
                            refreshing = false
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
                        copy(orderListState = OrderList.DataState.State.ERROR)
                    }
                }
            },
            onError = {
                // No idea how to handle this
            },
        )
    }

    private fun stopObservingOrderList() {
        orderListJob?.cancel()
        orderErrorJob?.cancel()

        orderListJob = null
        orderErrorJob = null
    }
}
