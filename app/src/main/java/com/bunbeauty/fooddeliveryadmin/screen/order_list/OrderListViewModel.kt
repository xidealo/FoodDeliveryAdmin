package com.bunbeauty.fooddeliveryadmin.screen.order_list

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.bunbeauty.data.repository.OrderRepository
import com.bunbeauty.domain.model.order.OrderListResult
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.CheckIsAnotherCafeSelectedUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.GetCafeListUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.GetIsLastOrderUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.NewOrderEventUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.SelectCafeUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.SetupCafesUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.SetupOrdersUseCase
import com.bunbeauty.presentation.Option
import com.bunbeauty.presentation.extension.mapToStateFlow
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val setupCafesUseCase: SetupCafesUseCase,
    private val selectCafeUseCase: SelectCafeUseCase,
    private val setupOrdersUseCase: SetupOrdersUseCase,
    private val getCafeListUseCase: GetCafeListUseCase,
    private val checkIsAnotherCafeSelectedUseCase: CheckIsAnotherCafeSelectedUseCase,
    private val newOrderEventUseCase: NewOrderEventUseCase,
    private val getIsLastOrderUseCase: GetIsLastOrderUseCase,
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

    init {
        observeOrderList()
        updateData()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        if (mutableDataState.value.cafeState != OrderListDataState.State.LOADING) {
            handleWithState {
                mutableDataState.update { state ->
                    state.copy(selectedCafe = selectCafeUseCase())
                }
                setupOrdersUseCase()
            }
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)

        viewModelScope.launch {
            orderRepository.unsubscribeOnOrderList("onStop")
        }
    }

    fun onCafeClicked() {
        viewModelScope.launch {
            val cafeList = getCafeListUseCase().map { cafe ->
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
        handleWithState {
            if (checkIsAnotherCafeSelectedUseCase(cafeUuid)) {
                mutableDataState.update { state ->
                    state.copy(selectedCafe = selectCafeUseCase(cafeUuid))
                }
                orderRepository.unsubscribeOnOrderList("new cafe selected")
                setupOrdersUseCase()
            }
        }
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
        updateData()
    }

    fun consumeEvents(events: List<OrderListEvent>) {
        mutableDataState.update { state ->
            state - events
        }
    }

    private fun updateData() {
        handleWithState {
            setupCafesUseCase()
            mutableDataState.update { state ->
                state.copy(selectedCafe = selectCafeUseCase())
            }
            setupOrdersUseCase()
        }
    }

    private fun observeOrderList() {
        orderRepository.orderListFlow.onEach { orderListResult ->
            when (orderListResult) {
                is OrderListResult.Success -> {
                    mutableDataState.update { state ->
                        state.copy(orderList = orderListResult.orderList)
                    }
                }
                OrderListResult.Error -> {
                    updateStateWithError()
                }
            }
        }.launchIn(viewModelScope)

        newOrderEventUseCase().onEach {
            mutableDataState.update { state ->
                state + OrderListEvent.ScrollToTop
            }
        }.launchIn(viewModelScope)
    }

    private suspend fun checkToCancelNotification(orderCode: String) {
        if (getIsLastOrderUseCase(orderCode = orderCode)) {
            mutableDataState.update { orderListState ->
                orderListState + OrderListEvent.CancelNotification
            }
        }
    }

    private inline fun handleWithState(crossinline block: suspend () -> Unit) {
        viewModelScope.launch {
            mutableDataState.update { state ->
                state.copy(cafeState = OrderListDataState.State.LOADING)
            }
            try {
                block()
                mutableDataState.update { state ->
                    state.copy(cafeState = OrderListDataState.State.SUCCESS)
                }
            } catch (exception: Exception) {
                updateStateWithError()
            }
        }
    }

    private fun updateStateWithError() {
        mutableDataState.update { state ->
            state.copy(cafeState = OrderListDataState.State.ERROR)
        }
    }
}
