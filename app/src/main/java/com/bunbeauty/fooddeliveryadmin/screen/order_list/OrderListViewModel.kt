package com.bunbeauty.fooddeliveryadmin.screen.order_list

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.bunbeauty.data.repository.OrderRepository
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.OrderListResult
import com.bunbeauty.domain.use_case.LogoutUseCase
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.CheckIsAnotherCafeSelectedUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.GetCafeListUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.NewOrderEventUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.SelectCafeUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.SetupCafesUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.SetupOrdersUseCase
import com.bunbeauty.fooddeliveryadmin.screen.order_list.list.OrderItemModel
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val stringUtil: IStringUtil,
    private val dateTimeUtil: IDateTimeUtil,
    private val setupCafesUseCase: SetupCafesUseCase,
    private val selectCafeUseCase: SelectCafeUseCase,
    private val setupOrdersUseCase: SetupOrdersUseCase,
    private val getCafeListUseCase: GetCafeListUseCase,
    private val checkIsAnotherCafeSelectedUseCase: CheckIsAnotherCafeSelectedUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val newOrderEventUseCase: NewOrderEventUseCase,
) : BaseViewModel(), DefaultLifecycleObserver {

    private val mutableOrderListState: MutableStateFlow<OrderListState> =
        MutableStateFlow(OrderListState())
    val orderListState: StateFlow<OrderListState> = mutableOrderListState.asStateFlow()

    init {
        observeOrderList()
        updateData()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        if (!mutableOrderListState.value.isLoading) {
            handleWithState {
                mutableOrderListState.update { state ->
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
            mutableOrderListState.update { state ->
                state + OrderListState.Event.OpenCafeListEvent(cafeList)
            }
        }
    }

    fun onCafeSelected(cafeUuid: String?) {
        handleWithState {
            if (checkIsAnotherCafeSelectedUseCase(cafeUuid)) {
                mutableOrderListState.update { state ->
                    state.copy(selectedCafe = selectCafeUseCase(cafeUuid))
                }
                orderRepository.unsubscribeOnOrderList("new cafe selected")
                setupOrdersUseCase()
            }
        }
    }

    fun onOrderClicked(orderUuid: String) {
        mutableOrderListState.update { state ->
            state + OrderListState.Event.OpenOrderDetailsEvent(orderUuid)
        }
    }

    fun onLogout(logoutOption: String) {
        if (LogoutOption.valueOf(logoutOption) == LogoutOption.LOGOUT) {
            handleWithState {
                logoutUseCase()
                mutableOrderListState.update { state ->
                    state + OrderListState.Event.OpenLoginEvent
                }
            }
        }
    }

    fun onRetryClicked() {
        updateData()
    }

    fun consumeEvents(events: List<OrderListState.Event>) {
        mutableOrderListState.update { state ->
            state - events
        }
    }

    private fun updateData() {
        handleWithState {
            setupCafesUseCase()
            mutableOrderListState.update { state ->
                state.copy(selectedCafe = selectCafeUseCase())
            }
            setupOrdersUseCase()
        }
    }

    private fun observeOrderList() {
        orderRepository.orderListFlow.onEach { orderListResult ->
            when (orderListResult) {
                is OrderListResult.Success -> {
                    val orderItemList = orderListResult.orderList.map(::toItemModel)
                    mutableOrderListState.update { state ->
                        state.copy(orderList = orderItemList)
                    }
                }
                OrderListResult.Error -> {
                    updateStateWithError()
                }
            }
        }.launchIn(viewModelScope)

        newOrderEventUseCase().onEach {
            mutableOrderListState.update { state ->
                state + OrderListState.Event.ScrollToTop
            }
        }.launchIn(viewModelScope)
    }

    private fun toItemModel(order: Order): OrderItemModel {
        return OrderItemModel(
            uuid = order.uuid,
            status = order.orderStatus,
            statusString = stringUtil.getOrderStatusString(order.orderStatus),
            code = order.code,
            deferredTime = stringUtil.getDeferredTimeString(order.deferredTime) ?: "",
            time = dateTimeUtil.getDateTimeDDMMHHMM(order.time)
        )
    }

    private inline fun handleWithState(crossinline block: suspend () -> Unit) {
        viewModelScope.launch {
            mutableOrderListState.update { state ->
                state.copy(isLoading = true)
            }
            try {
                block()
                mutableOrderListState.update { state ->
                    state.copy(isLoading = false)
                }
            } catch (exception: Exception) {
                updateStateWithError()
            }
        }
    }

    private fun updateStateWithError() {
        mutableOrderListState.update { state ->
            state.copy(
                isLoading = false,
                orderList = emptyList()
            ) + OrderListState.Event.ShowError
        }
    }
}
