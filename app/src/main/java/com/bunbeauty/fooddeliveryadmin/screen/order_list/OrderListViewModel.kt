package com.bunbeauty.fooddeliveryadmin.screen.order_list

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.bunbeauty.data.repository.CafeRepository
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
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
    private val orderRepo: OrderRepo,
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
            mutableOrderListState.update { orderListState ->
                val openCafeListEvent = OrderListState.Event.OpenCafeListEvent(cafeList)
                orderListState.copy(eventList = orderListState.eventList + openCafeListEvent)
            }
        }
    }

    fun onCafeSelected(cafeUuid: String?) {
        val selectedCafeUuid = mutableOrderListState.value.selectedCafe?.uuid

        if (cafeUuid != null && cafeUuid != selectedCafeUuid) {
            mutableOrderListState.update { orderListState ->
                orderListState.copy(isLoading = true)
            }

            viewModelScope.launch {
                dataStoreRepo.saveCafeUuid(cafeUuid)
                refreshSelectedCafe()
            }
        }
    }

    fun consumeEvents(events: List<OrderListState.Event>) {
        mutableOrderListState.update { orderListState ->
            orderListState.copy(eventList = orderListState.eventList - events.toSet())
        }
    }

    private fun unsubscribe(cafeUuid: String?, message: String) {
        Log.d("testTag", "unsubscribe $cafeUuid")
        if (cafeUuid != null) {
            viewModelScope.launch {
                orderRepo.unsubscribeOnOrderList(cafeId = cafeUuid, message = message)
                orderRepo.unsubscribeOnNotification(cafeId = cafeUuid)
            }
        }
    }

    private fun subscribe(cafeUuid: String?) {
        Log.d("testTag", "subscribe $cafeUuid")
        if (cafeUuid != null) {
            viewModelScope.launch {
                val token = dataStoreRepo.token.first()
                launch {
                    orderRepo.subscribeOnOrderList(token = token, cafeId = cafeUuid)
                }
                launch {
                    orderRepo.subscribeOnNotification(cafeId = cafeUuid)
                }
                launch {
                    orderRepo.loadOrderListByCafeId(token = token, cafeId = cafeUuid)
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
        unsubscribe(mutableOrderListState.value.selectedCafe?.uuid, "refresh selected cafe")
        val selectedCafe = dataStoreRepo.cafeUuid.first()?.let { cafeUuid ->
            cafeRepository.getCafeByUuid(cafeUuid)?.address?.let { address ->
                SelectedCafe(
                    uuid = cafeUuid,
                    title = address
                )
            }
        } ?: run {
            cafeRepository.getCafeList().firstOrNull()?.let { firstCafe ->
                SelectedCafe(
                    uuid = firstCafe.uuid,
                    title = firstCafe.address
                )
            }
        }
        mutableOrderListState.update { orderListState ->
            orderListState.copy(selectedCafe = selectedCafe, isLoading = false)
        }
        subscribe(selectedCafe?.uuid)
    }

    private fun observeOrderList() {
        Log.d("testTag", "observeOrderList")
        orderRepo.orderListFlow.onEach { orderList ->
            mutableOrderListState.update { orderListState ->
                Log.d("testTag", "update orderList ${orderList.size}")
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
            deferredTime = stringUtil.getDeferredTimeString(order.deferred) ?: "",
            time = dateTimeUtil.getDateTimeDDMMHHMM(order.time)
        )
    }

}