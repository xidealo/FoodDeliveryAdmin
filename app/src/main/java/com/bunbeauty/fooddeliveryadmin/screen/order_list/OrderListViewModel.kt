package com.bunbeauty.fooddeliveryadmin.screen.order_list

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.bunbeauty.data.repository.CafeRepository
import com.bunbeauty.data.repository.OrderRepository
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.model.order.OrderDetails
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderListViewModel @Inject constructor(
    private val orderRepository: OrderRepository,
    private val cafeRepository: CafeRepository,
    private val dataStoreRepo: DataStoreRepo,
    private val stringUtil: IStringUtil,
    private val dateTimeUtil: IDateTimeUtil,
) : BaseViewModel(), DefaultLifecycleObserver {

    private val mutableOrderListState: MutableStateFlow<OrderListState> =
        MutableStateFlow(OrderListState())
    val orderListState: StateFlow<OrderListState> = mutableOrderListState.asStateFlow()

    init {
        setupSelectedCafe()
        observeOrderList()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        subscribe(mutableOrderListState.value.selectedCafe?.uuid)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        unsubscribe(mutableOrderListState.value.selectedCafe?.uuid, "fragment onStop")
    }

    fun onCafeClicked() {
        viewModelScope.launch {
            val cafeList = cafeRepository.getCafeList().map { cafe ->
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
        val selectedCafeUuid = mutableOrderListState.value.selectedCafe?.uuid

        if (cafeUuid != null && cafeUuid != selectedCafeUuid) {
            viewModelScope.launch {
                dataStoreRepo.saveCafeUuid(cafeUuid)
                refreshSelectedCafe()
            }
        }
    }

    fun onOrderClicked(orderUuid: String) {
        val openOrderDetailsEvent = OrderListState.Event.OpenOrderDetailsEvent(orderUuid)
        mutableOrderListState.update { orderListState ->
            orderListState.copy(eventList = orderListState.eventList + openOrderDetailsEvent)
        }
    }

    fun consumeEvents(events: List<OrderListState.Event>) {
        mutableOrderListState.update { orderListState ->
            orderListState.copy(eventList = orderListState.eventList - events.toSet())
        }
    }

    private fun unsubscribe(cafeUuid: String?, message: String) {
        if (cafeUuid != null) {
            viewModelScope.launch {
                orderRepository.unsubscribeOnOrderList(cafeId = cafeUuid, message = message)
                orderRepository.unsubscribeOnNotification(cafeId = cafeUuid)
            }
        }
    }

    private fun subscribe(cafeUuid: String?) {
        if (cafeUuid != null) {
            viewModelScope.launch {
                val token = dataStoreRepo.token.first()
                launch {
                    orderRepository.subscribeOnOrderList(token = token, cafeId = cafeUuid)
                }
                launch {
                    orderRepository.subscribeOnNotification(cafeId = cafeUuid)
                }
                launch {
                    orderRepository.loadOrderListByCafeUuid(token = token, cafeUuid = cafeUuid)
                }
            }
        }
    }

    private fun setupSelectedCafe() {
        mutableOrderListState.update { orderListState ->
            orderListState.copy(isLoading = true)
        }
        viewModelScope.launch {
            cafeRepository.refreshCafeList(
                token = dataStoreRepo.token.first(),
                cityUuid = dataStoreRepo.managerCity.first()
            )
            refreshSelectedCafe()
        }
    }

    private suspend fun refreshSelectedCafe() {
        mutableOrderListState.update { orderListState ->
            orderListState.copy(isLoading = true)
        }
        unsubscribe(mutableOrderListState.value.selectedCafe?.uuid, "refresh selected cafe")
        val selectedCafe = dataStoreRepo.cafeUuid.first()?.let { cafeUuid ->
            cafeRepository.getCafeByUuid(cafeUuid)?.address?.let { address ->
                SelectedCafe(
                    uuid = cafeUuid,
                    address = address
                )
            }
        } ?: run {
            cafeRepository.getCafeList().firstOrNull()?.let { firstCafe ->
                SelectedCafe(
                    uuid = firstCafe.uuid,
                    address = firstCafe.address
                )
            }
        }
        mutableOrderListState.update { orderListState ->
            orderListState.copy(selectedCafe = selectedCafe, isLoading = false)
        }
        subscribe(selectedCafe?.uuid)
    }

    private fun observeOrderList() {
        orderRepository.orderListFlow.onEach { orderList ->
            mutableOrderListState.update { orderListState ->
                val processedOrderList = orderList.map(::toItemModel)
                    .filter { orderItemModel ->
                        orderItemModel.status != OrderStatus.CANCELED
                    }
                orderListState.copy(orderList = processedOrderList)
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

}