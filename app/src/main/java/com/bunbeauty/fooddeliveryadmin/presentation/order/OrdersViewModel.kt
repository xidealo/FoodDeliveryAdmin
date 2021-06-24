package com.bunbeauty.fooddeliveryadmin.presentation.order

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.fooddeliveryadmin.extensions.toStateAddedSuccess
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.fooddeliveryadmin.extensions.toStateUpdatedSuccess
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.state.ExtendedState
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.OrderItem
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrdersFragmentDirections.toAddressListBottomSheet
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrdersFragmentDirections.toOrdersDetailsFragment
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderRepo: OrderRepo,
    private val cafeRepo: CafeRepo,
    private val stringUtil: IStringUtil,
    private val dateTimeUtil: IDateTimeUtil,
    private val dataStoreRepo: DataStoreRepo
) : BaseViewModel() {

    private val _cafeAddressStateFlow: MutableStateFlow<State<String>> =
        MutableStateFlow(State.Loading())
    val cafeAddressStateFlow: StateFlow<State<String>>
        get() = _cafeAddressStateFlow.asStateFlow()

    private val _orderListState: MutableStateFlow<ExtendedState<List<OrderItem>>> =
        MutableStateFlow(ExtendedState.Loading())
    val orderListState: StateFlow<ExtendedState<List<OrderItem>>>
        get() = _orderListState.asStateFlow()

    init {
        subscribeOnAddress()
        subscribeOnOrders()
    }

    fun subscribeOnAddress() {
        dataStoreRepo.cafeId.flatMapLatest { cafeId ->
            cafeRepo.getCafeByIdFlow(cafeId)
        }.onEach { cafe ->
            if (cafe != null) {
                _cafeAddressStateFlow.value = stringUtil.toString(cafe.address).toStateSuccess()
            }
        }.launchIn(viewModelScope)
    }

    fun subscribeOnOrders() {
        dataStoreRepo.cafeId.flatMapLatest { cafeId ->
            orderRepo.getAddedOrderListByCafeId(cafeId)
        }.onEach { orderList ->
            _orderListState.value = toOrderItemList(orderList).toStateAddedSuccess()
        }.launchIn(viewModelScope)

        dataStoreRepo.cafeId.flatMapLatest { cafeId ->
            orderRepo.getUpdatedOrderListByCafeId(cafeId)
        }.onEach { orderList ->
            _orderListState.value = toOrderItemList(orderList).toStateUpdatedSuccess()
        }.launchIn(viewModelScope)
    }

    fun toOrderItemList(orderList: List<Order>): List<OrderItem> {
        return orderList.map { order ->
            OrderItem(
                status = order.orderEntity.orderStatus,
                statusString = stringUtil.getOrderStatusString(order.orderEntity.orderStatus),
                code = order.orderEntity.code,
                deferredTime = stringUtil.getDeferredTimeString(order.orderEntity.deferred),
                time = dateTimeUtil.getDateTimeDDMMHHMM(order.timestamp),
                order = order
            )
        }
    }

    fun goToAddressList() {
        router.navigate(toAddressListBottomSheet())
    }

    fun goToOrderDetails(order: Order) {
        router.navigate(toOrdersDetailsFragment(order))
    }
}