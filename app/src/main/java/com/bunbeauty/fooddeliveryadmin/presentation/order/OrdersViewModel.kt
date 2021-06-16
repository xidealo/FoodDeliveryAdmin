package com.bunbeauty.fooddeliveryadmin.presentation.order

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.ExtendedState
import com.bunbeauty.common.State
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.date_time.DateTimeUtil
import com.bunbeauty.domain.repository.cafe.CafeRepo
import com.bunbeauty.domain.repository.order.OrderRepo
import com.bunbeauty.fooddeliveryadmin.extensions.toStateAddedSuccess
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.fooddeliveryadmin.extensions.toStateUpdatedSuccess
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.OrderItem
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

abstract class OrdersViewModel : BaseViewModel() {

    abstract val cafeAddressStateFlow: StateFlow<State<String>>
    abstract val orderListState: StateFlow<ExtendedState<List<OrderItem>>>

//    abstract fun subscribeOnAddress()
//    abstract fun subscribeOnOrders()
}

@ExperimentalCoroutinesApi
class OrdersViewModelImpl @Inject constructor(
    private val orderRepo: OrderRepo,
    private val cafeRepo: CafeRepo,
    private val stringUtil: IStringUtil,
    private val dateTimeUtil: DateTimeUtil,
    private val dataStoreHelper: IDataStoreHelper
) : OrdersViewModel() {

    override val cafeAddressStateFlow =
        MutableStateFlow<State<String>>(State.Loading())

    override val orderListState =
        MutableStateFlow<ExtendedState<List<OrderItem>>>(ExtendedState.Loading())

    init {
        subscribeOnAddress()
        subscribeOnOrders()
    }

    fun subscribeOnAddress() {
        dataStoreHelper.cafeId.flatMapLatest { cafeId ->
            cafeRepo.getCafeByIdFlow(cafeId)
        }.onEach { cafe ->
            orderListState.value = ExtendedState.Loading()

            if (cafe != null) {
                cafeAddressStateFlow.value = stringUtil.toString(cafe.address).toStateSuccess()
            }
        }.launchIn(viewModelScope)
    }

    fun subscribeOnOrders() {
        dataStoreHelper.cafeId.flatMapLatest { cafeId ->
            orderRepo.getAddedOrderListByCafeId(cafeId)
        }.onEach { orderList ->
            orderListState.value = toOrderItemList(orderList).toStateAddedSuccess()
        }.launchIn(viewModelScope)

        dataStoreHelper.cafeId.flatMapLatest { cafeId ->
            orderRepo.getUpdatedOrderListByCafeId(cafeId)
        }.onEach { orderList ->
            orderListState.value = toOrderItemList(orderList).toStateUpdatedSuccess()
        }.launchIn(viewModelScope)
    }

    fun toOrderItemList(orderList: List<Order>): List<OrderItem> {
        return orderList.map { order ->
            OrderItem(
                status = order.orderEntity.orderStatus,
                code = order.orderEntity.code,
                deferredTime = stringUtil.getDeferredTimeString(order.orderEntity.deferred),
                time = dateTimeUtil.getTimeHHMM(order.timestamp),
                order = order
            )
        }
    }
}