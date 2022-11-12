package com.bunbeauty.fooddeliveryadmin.screen.order_list

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.bunbeauty.data.repository.OrderRepository
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.fooddeliveryadmin.domain.LogoutUseCase
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.fooddeliveryadmin.screen.order_list.domain.*
import com.bunbeauty.fooddeliveryadmin.screen.order_list.list.OrderItemModel
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
        withLoading {
            setupCafesUseCase()
            mutableOrderListState.update { state ->
                state.copy(selectedCafe = selectCafeUseCase())
            }
            setupOrdersUseCase()
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        if (!mutableOrderListState.value.isLoading) {
            withLoading {
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
            val openCafeListEvent = OrderListState.Event.OpenCafeListEvent(cafeList)
            mutableOrderListState.update { orderListState ->
                orderListState.copy(eventList = orderListState.eventList + openCafeListEvent)
            }
        }
    }

    fun onCafeSelected(cafeUuid: String?) {
        withLoading {
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
        val openOrderDetailsEvent = OrderListState.Event.OpenOrderDetailsEvent(orderUuid)
        mutableOrderListState.update { orderListState ->
            orderListState.copy(eventList = orderListState.eventList + openOrderDetailsEvent)
        }
    }

    fun onLogout(logoutOption: String) {
        if (LogoutOption.valueOf(logoutOption) == LogoutOption.LOGOUT) {
            withLoading {
                logoutUseCase()
                val event = OrderListState.Event.OpenLoginEvent
                mutableOrderListState.update { orderListState ->
                    orderListState.copy(eventList = orderListState.eventList + event)
                }
            }
        }
    }

    fun consumeEvents(events: List<OrderListState.Event>) {
        mutableOrderListState.update { orderListState ->
            orderListState.copy(eventList = orderListState.eventList - events.toSet())
        }
    }

    private fun observeOrderList() {
        orderRepository.orderListFlow.onEach { orderList ->
            val orderItemList = orderList.map(::toItemModel)
            mutableOrderListState.update { state ->
                state.copy(orderList = orderItemList)
            }
        }.launchIn(viewModelScope)

        newOrderEventUseCase().onEach {
            mutableOrderListState.update { state ->
                state.copy(eventList = state.eventList + OrderListState.Event.ScrollToTop)
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

    private inline fun withLoading(crossinline block: suspend () -> Unit) {
        viewModelScope.launch {
            mutableOrderListState.update { state ->
                state.copy(isLoading = true)
            }
            block()
            mutableOrderListState.update { state ->
                state.copy(isLoading = false)
            }
        }
    }

}