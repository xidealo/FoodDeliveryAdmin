package com.bunbeauty.presentation.feature.orderlist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.common.GetCafeListUseCase
import com.bunbeauty.domain.feature.orderlist.CheckIsAnotherCafeSelectedUseCase
import com.bunbeauty.domain.feature.orderlist.GetOrderErrorFlowUseCase
import com.bunbeauty.domain.feature.orderlist.GetOrderListFlowUseCase
import com.bunbeauty.domain.feature.orderlist.GetSelectedCafeUseCase
import com.bunbeauty.domain.feature.orderlist.SaveSelectedCafeUuidUseCase
import com.bunbeauty.domain.model.cafe.SelectedCafe
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.extension.mapToStateFlow
import com.bunbeauty.presentation.feature.orderlist.mapper.OrderMapper
import com.bunbeauty.presentation.feature.orderlist.state.OrderList
import com.bunbeauty.presentation.feature.orderlist.state.OrderListUiState
import com.bunbeauty.presentation.feature.selectcafe.SelectableCafeItem
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val getOrderListFlow: GetOrderListFlowUseCase,
    private val getOrderErrorFlow: GetOrderErrorFlowUseCase,
    private val getSelectedCafe: GetSelectedCafeUseCase,
    private val getCafeList: GetCafeListUseCase,
    private val saveSelectedCafeUuid: SaveSelectedCafeUuidUseCase,
    private val checkIsAnotherCafeSelected: CheckIsAnotherCafeSelectedUseCase,
    private val orderMapper: OrderMapper
) : BaseStateViewModel<OrderList.DataState, OrderList.Action, OrderList.Event>(
    initState = OrderList.DataState(
        refreshing = false,
        selectedCafe = null,
        cafeState = OrderList.DataState.State.LOADING,
        orderList = null,
        orderListState = OrderList.DataState.State.LOADING,
    )
) {

    override fun reduce(action: OrderList.Action, dataState: OrderList.DataState) {
        when (action) {
            OrderList.Action.StartObserveOrders -> observeOrderList(
                selectedCafe = dataState.selectedCafe ?: return
            )

            OrderList.Action.StopObserveOrders -> stopObservingOrderList()
            OrderList.Action.RetryClick -> onRetryClicked()
        }
    }

    val orderListUiState = mutableDataState.mapToStateFlow(viewModelScope) { dataState ->
        OrderListUiState(
            state = when (dataState.cafeState) {
                OrderList.DataState.State.LOADING -> OrderListUiState.State.Loading
                OrderList.DataState.State.ERROR -> OrderListUiState.State.Error
                OrderList.DataState.State.SUCCESS -> OrderListUiState.State.Success(
                    cafeAddress = dataState.selectedCafe?.address ?: "",
                    orderList = dataState.orderList?.map(orderMapper::map) ?: emptyList()
                )
            },
            connectionError = dataState.orderListState == OrderList.DataState.State.ERROR,
            refreshing = dataState.refreshing,
        )
    }

    private var orderListJob: Job? = null
    private var orderErrorJob: Job? = null

    init {
        setUpCafe()
    }

    fun onRefresh() {
        setState {
            copy(refreshing = true)
        }
        stopObservingOrderList()
        setUpCafe()
    }

    fun retrySetUp() {
        stopObservingOrderList()
        setUpCafe()
    }

    fun onCafeClicked() {
        viewModelScope.launchSafe(
            onError = {
                // No idea how to handle this
            },
            block = {
                val cafeList = getCafeList().map { cafe ->
                    SelectableCafeItem(
                        uuid = cafe.uuid,
                        address = cafe.address,
                        isSelected = cafe.uuid == mutableDataState.value.selectedCafe?.uuid
                    )
                }
                sendEvent {
                    OrderList.Event.OpenCafeListEvent(cafeList)
                }
            }
        )
    }

    fun onCafeSelected(cafeUuid: String?) {
        cafeUuid ?: return

        viewModelScope.launchSafe(
            onError = {
                // No idea how to handle this
            },
            block = {
                if (checkIsAnotherCafeSelected(cafeUuid)) {
                    stopObservingOrderList()
                    saveSelectedCafeUuid(cafeUuid)
                    setUpCafe()
                }
            }
        )
    }

    fun onOrderClicked(orderItem: OrderListUiState.OrderItem) {
        sendEvent {
            OrderList.Event.OpenOrderDetailsEvent(
                orderUuid = orderItem.uuid,
                orderCode = orderItem.code
            )
        }
        sendEvent {
            OrderList.Event.CancelNotification(
                notificationId = orderItem.code.hashCode()
            )
        }
    }

    fun onRetryClicked(selectedCafe: SelectedCafe) {
        stopObservingOrderList()
        setUpCafe(selectedCafe = selectedCafe)
    }

    private fun setUpCafe() {
        viewModelScope.launchSafe(
            onError = {
                setState {
                    copy(cafeState = OrderList.DataState.State.ERROR)
                }
            },
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

                observeOrderList(selectedCafe = selectedCafe)
            }
        )
    }

    private fun observeOrderList(selectedCafe: SelectedCafe) {

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
                getOrderListFlow(selectedCafe.uuid).collect { orderList ->
                    setState {
                        copy(
                            orderList = orderList,
                            refreshing = false
                        )
                    }

//                    if (!state.orderList.isNullOrEmpty() &&
//                        (state.orderList.size < orderList.size)
//                    ) {
//                        newState + OrderList.Event.ScrollToTop
//                    } else {
//                        newState
//                    }

                }
            }
        )

        orderErrorJob = viewModelScope.launchSafe(
            onError = {
                // No idea how to handle this
            },
            block = {
                getOrderErrorFlow().collect {
                    setState {
                        copy(orderListState = OrderList.DataState.State.ERROR)
                    }
                }
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
