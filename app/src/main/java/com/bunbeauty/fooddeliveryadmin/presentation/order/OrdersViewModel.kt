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

    private val _cafeAddressState: MutableStateFlow<State<String>> =
        MutableStateFlow(State.Loading())
    val cafeAddressState: StateFlow<State<String>>
        get() = _cafeAddressState.asStateFlow()

    private val _orderListState: MutableStateFlow<ExtendedState<List<OrderItem>>> =
        MutableStateFlow(ExtendedState.Loading())
    val orderListState: StateFlow<ExtendedState<List<OrderItem>>>
        get() = _orderListState.asStateFlow()

    init {
        subscribeOnAddress()
        subscribeOnOrders()
    }

    fun subscribeOnAddress() {
        dataStoreRepo.cafeUuid.flatMapLatest { cafeId ->
            cafeRepo.getCafeByUuid(cafeId)
        }.onEach { cafe ->
            if (cafe != null) {
                _cafeAddressState.value = cafe.address.toStateSuccess()
            }
        }.launchIn(viewModelScope)
    }

    fun subscribeOnOrders() {
        dataStoreRepo.cafeUuid.flatMapLatest { cafeId ->
            orderRepo.getAddedOrderListByCafeId(cafeId)
        }.onEach { orderList ->
            _orderListState.value = toOrderItemList(orderList).toStateAddedSuccess()
        }.launchIn(viewModelScope)

        dataStoreRepo.cafeUuid.flatMapLatest { cafeId ->
            orderRepo.getUpdatedOrderListByCafeId(cafeId)
        }.onEach { orderList ->
            _orderListState.value = toOrderItemList(orderList).toStateUpdatedSuccess()
        }.launchIn(viewModelScope)
    }

    fun toOrderItemList(orderList: List<Order>): List<OrderItem> {
        return orderList.map { order ->
            OrderItem(
                status = order.orderStatus,
                statusString = stringUtil.getOrderStatusString(order.orderStatus),
                code = order.code,
                deferredTime = stringUtil.getDeferredTimeString(order.deferred),
                time = dateTimeUtil.getDateTimeDDMMHHMM(order.time),
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