package com.bunbeauty.fooddeliveryadmin.screen.order_list

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.order_list.GetOrderErrorFlowUseCase
import com.bunbeauty.domain.feature.order_list.GetOrderListFlowUseCase
import com.bunbeauty.domain.feature.order_list.GetCafeListUseCase
import com.bunbeauty.domain.feature.order_list.GetIsLastOrderUseCase
import com.bunbeauty.domain.feature.order_list.GetSelectedCafeUseCase
import com.bunbeauty.domain.feature.order_list.SaveSelectedCafeUuidUseCase
import com.bunbeauty.domain.feature.order_list.CheckIsAnotherCafeSelectedUseCase
import com.bunbeauty.domain.feature.order_list.SubscribeToCafeNotificationUseCase
import com.bunbeauty.domain.feature.order_list.UnsubscribeFromCafeNotificationUseCase
import com.bunbeauty.presentation.Option
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.extension.mapToStateFlow
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val getOrderListFlow: GetOrderListFlowUseCase,
    private val getOrderErrorFlow: GetOrderErrorFlowUseCase,
    private val getSelectedCafe: GetSelectedCafeUseCase,
    private val getCafeList: GetCafeListUseCase,
    private val saveSelectedCafeUuid: SaveSelectedCafeUuidUseCase,
    private val getIsLastOrder: GetIsLastOrderUseCase,
    private val checkIsAnotherCafeSelected: CheckIsAnotherCafeSelectedUseCase,
    private val unsubscribeFromCafeNotification: UnsubscribeFromCafeNotificationUseCase,
    private val subscribeToCafeNotification: SubscribeToCafeNotificationUseCase,
    private val orderMapper: OrderMapper,
) : BaseViewModel(), DefaultLifecycleObserver {

    private val mutableDataState = MutableStateFlow(
        OrderListDataState(
            selectedCafe = null,
            cafeState = OrderListDataState.State.LOADING,
            orderList = null,
            orderListState = OrderListDataState.State.LOADING,
            eventList = emptyList(),
        )
    )
    val orderListUiState = mutableDataState.mapToStateFlow(viewModelScope) { dataState ->
        OrderListUiState(
            state = when (dataState.cafeState) {
                OrderListDataState.State.LOADING -> OrderListUiState.State.Loading
                OrderListDataState.State.ERROR -> OrderListUiState.State.Error
                OrderListDataState.State.SUCCESS -> OrderListUiState.State.Success(
                    cafeAddress = dataState.selectedCafe?.address ?: "",
                    orderList = dataState.orderList?.map(orderMapper::map) ?: emptyList(),
                )
            },
            eventList = dataState.eventList
        )
    }

    private var orderListJob: Job? = null
    private var orderErrorJob: Job? = null

    init {
        setUpCafe()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        observeOrderList()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)

        stopObservingOrderList()
    }

    fun onCafeClicked() {
        viewModelScope.launch {
            val cafeList = getCafeList().map { cafe ->
                Option(
                    id = cafe.uuid,
                    title = cafe.address,
                )
            }
            mutableDataState.update { state ->
                state + OrderListEvent.OpenCafeListEvent(cafeList)
            }
        }
    }

    fun onCafeSelected(cafeUuid: String?) {
        cafeUuid ?: return

        viewModelScope.launchSafe(
            onError = {},
            block = {
                if (checkIsAnotherCafeSelected(cafeUuid)) {
                    stopObservingOrderList()
                    unsubscribeFromCafeNotification()
                    saveSelectedCafeUuid(cafeUuid)
                    setUpCafe()
                }
            }
        )
    }

    fun onOrderClicked(orderItem: OrderListUiState.OrderItem) {
        mutableDataState.update { state ->
            state + OrderListEvent.OpenOrderDetailsEvent(
                orderUuid = orderItem.uuid,
                orderCode = orderItem.code,
            )
        }
        viewModelScope.launch {
            checkToCancelNotification(orderItem.code)
        }
    }

    fun onRetryClicked() {
        setUpCafe()
    }

    fun consumeEvents(events: List<OrderListEvent>) {
        mutableDataState.update { state ->
            state - events
        }
    }

    private fun setUpCafe() {
        viewModelScope.launchSafe(
            onError = {
                mutableDataState.update { state ->
                    state.copy(cafeState = OrderListDataState.State.ERROR)
                }
            },
            block = {
                mutableDataState.update { state ->
                    state.copy(cafeState = OrderListDataState.State.LOADING)
                }
                val selectedCafe = getSelectedCafe()
                mutableDataState.update { state ->
                    if (selectedCafe == null) {
                        state.copy(cafeState = OrderListDataState.State.ERROR)
                    } else {
                        subscribeToCafeNotification(selectedCafe.uuid)
                        state.copy(
                            cafeState = OrderListDataState.State.SUCCESS,
                            selectedCafe = selectedCafe,
                        )
                    }
                }

                observeOrderList()
            }
        )
    }

    private fun observeOrderList() {
        val selectedCafe = mutableDataState.value.selectedCafe ?: return

        orderListJob = viewModelScope.launchSafe(
            onError = {
                mutableDataState.update { state ->
                    state.copy(orderListState = OrderListDataState.State.ERROR)
                }
            },
            block = {
                getOrderListFlow(selectedCafe.uuid).collect { orderList ->
                    mutableDataState.update { state ->
                        state.copy(orderList = orderList)
                    }
                }
            }
        )

        orderErrorJob = viewModelScope.launchSafe(
            onError = {},
            block = {
                getOrderErrorFlow().collect {
                    mutableDataState.update { state ->
                        state.copy(orderListState = OrderListDataState.State.ERROR)
                    }
                }
            }
        )
    }

    private fun stopObservingOrderList() {
        orderListJob?.cancel()
        orderErrorJob?.cancel()
    }

    private suspend fun checkToCancelNotification(orderCode: String) {
        if (getIsLastOrder(orderCode = orderCode)) {
            mutableDataState.update { orderListState ->
                orderListState + OrderListEvent.CancelNotification
            }
        }
    }

}
