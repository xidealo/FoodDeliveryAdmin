package com.bunbeauty.fooddeliveryadmin.presentation.order

import androidx.lifecycle.viewModelScope
import com.bunbeauty.fooddeliveryadmin.presentation.state.ExtendedState
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.util.date_time.DateTimeUtil
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.fooddeliveryadmin.extensions.toStateAddedSuccess
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.fooddeliveryadmin.extensions.toStateUpdatedSuccess
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.OrderItem
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrdersFragmentDirections.*
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

abstract class OrdersViewModel : BaseViewModel() {

    abstract val cafeAddressStateFlow: StateFlow<State<String>>
    abstract val orderListState: StateFlow<ExtendedState<List<OrderItem>>>

    abstract fun subscribeOnAddress()
    abstract fun subscribeOnOrders()
    abstract fun goToAddressList()
    abstract fun goToOrderDetails(order: Order)
}

@ExperimentalCoroutinesApi
class OrdersViewModelImpl @Inject constructor(
    private val orderRepo: OrderRepo,
    private val cafeRepo: CafeRepo,
    private val stringUtil: IStringUtil,
    private val dateTimeUtil: DateTimeUtil,
    private val dataStoreRepo: DataStoreRepo
) : OrdersViewModel() {

    override val cafeAddressStateFlow: MutableStateFlow<State<String>> =
        MutableStateFlow(State.Loading())

    override val orderListState: MutableStateFlow<ExtendedState<List<OrderItem>>> =
        MutableStateFlow(ExtendedState.Loading())

    override fun subscribeOnAddress() {
        dataStoreRepo.cafeId.flatMapLatest { cafeId ->
            cafeRepo.getCafeByIdFlow(cafeId)
        }.onEach { cafe ->
            if (cafe != null) {
                cafeAddressStateFlow.value = stringUtil.toString(cafe.address).toStateSuccess()
            }
        }.launchIn(viewModelScope)
    }

    override fun subscribeOnOrders() {
        dataStoreRepo.cafeId.flatMapLatest { cafeId ->
            orderRepo.getAddedOrderListByCafeId(cafeId)
        }.onEach { orderList ->
            orderListState.value = toOrderItemList(orderList).toStateAddedSuccess()
        }.launchIn(viewModelScope)

        dataStoreRepo.cafeId.flatMapLatest { cafeId ->
            orderRepo.getUpdatedOrderListByCafeId(cafeId)
        }.onEach { orderList ->
            orderListState.value = toOrderItemList(orderList).toStateUpdatedSuccess()
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

    override fun goToAddressList() {
        router.navigate(toAddressListBottomSheet())
    }

    override fun goToOrderDetails(order: Order) {
        router.navigate(toOrdersDetailsFragment(order))
    }
}