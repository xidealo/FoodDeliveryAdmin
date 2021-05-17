package com.bunbeauty.fooddeliveryadmin.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.ExtendedState
import com.bunbeauty.common.State
import com.bunbeauty.common.extensions.toStateAddedSuccess
import com.bunbeauty.common.extensions.toStateSuccess
import com.bunbeauty.common.extensions.toStateUpdatedSuccess
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.model.Cafe
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import com.bunbeauty.domain.repository.cafe.CafeRepo
import com.bunbeauty.domain.repository.order.OrderRepo
import com.bunbeauty.domain.string_helper.IStringHelper
import com.bunbeauty.fooddeliveryadmin.ui.adapter.OrderItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

abstract class OrdersViewModel : BaseViewModel() {

    abstract val cafeStateFlow: StateFlow<State<Cafe>>
    abstract val orderListState: StateFlow<ExtendedState<List<OrderItem>>>

    abstract fun subscribeOnAddress()
    abstract fun subscribeOnOrders()
}

@ExperimentalCoroutinesApi
class OrdersViewModelImpl @Inject constructor(
    private val apiRepository: IApiRepository,
    private val orderRepo: OrderRepo,
    private val cafeRepo: CafeRepo,
    private val stringHelper: IStringHelper,
    private val dataStoreHelper: IDataStoreHelper
) : OrdersViewModel() {

    override val cafeStateFlow = MutableStateFlow<State<Cafe>>(State.Loading())
    override val orderListState =
        MutableStateFlow<ExtendedState<List<OrderItem>>>(ExtendedState.Loading())

    override fun subscribeOnAddress() {
        dataStoreHelper.cafeId.flatMapLatest { cafeId ->
            cafeRepo.getCafeByIdFlow(cafeId)
        }.onEach { cafe ->
            orderListState.value = ExtendedState.Loading()

            if (cafe != null) {
                cafeStateFlow.value = cafe.toStateSuccess()
            }
        }.launchIn(viewModelScope)
    }

    override fun subscribeOnOrders() {
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
                deferredTime = stringHelper.toStringDeferred(order.orderEntity),
                time = stringHelper.toStringTime(order),
                isDelivery = order.orderEntity.isDelivery,
                comment = order.orderEntity.comment,
                email = order.orderEntity.email,
                phone = order.orderEntity.phone,
                address = stringHelper.toString(order.orderEntity.address),
                cartProductList = order.cartProducts
            )
        }
    }

    fun removeOrder(order: Order) {
        apiRepository.delete(order)
    }
}