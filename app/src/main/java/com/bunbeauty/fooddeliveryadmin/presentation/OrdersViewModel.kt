package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.State
import com.bunbeauty.common.extensions.toStateNullableSuccess
import com.bunbeauty.common.extensions.toStateSuccess
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.model.Cafe
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import com.bunbeauty.domain.repository.cafe.CafeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class OrdersViewModel : BaseViewModel() {

    abstract val cafeStateFlow: StateFlow<State<Cafe>>
    abstract val addedOrderListStateFlow: StateFlow<State<List<Order>>>
    abstract val updatedOrderListStateFlow: StateFlow<State<List<Order>>>

    abstract fun getOrders(cafeId: String)
    abstract fun getAddress()
}

class OrdersViewModelImpl @Inject constructor(
    private val apiRepository: IApiRepository,
    private val cafeRepo: CafeRepo,
    private val dataStoreHelper: IDataStoreHelper
) : OrdersViewModel() {

    override val cafeStateFlow: MutableStateFlow<State<Cafe>> = MutableStateFlow(State.Loading())

    override val addedOrderListStateFlow: MutableStateFlow<State<List<Order>>> =
        MutableStateFlow(State.Loading())

    override val updatedOrderListStateFlow: MutableStateFlow<State<List<Order>>> =
        MutableStateFlow(State.Loading())

    override fun getAddress() {
        dataStoreHelper.cafeId.onEach { currentCafeId ->
            cafeRepo.cafeListFlow.onEach { cafeList ->
                val cafe = cafeList.find { cafe ->
                    cafe.cafeEntity.id == currentCafeId
                }
                if (cafe != null)
                    cafeStateFlow.value = cafe.toStateSuccess()
            }.launchIn(viewModelScope)
        }.launchIn(viewModelScope)
    }

    override fun getOrders(cafeId: String) {
        apiRepository.updatedOrderListStateFlow.onEach { orderList ->
            updatedOrderListStateFlow.value =
                orderList.filter { it.orderEntity.orderStatus != OrderStatus.CANCELED }
                    .toStateSuccess()
        }.launchIn(viewModelScope)

        apiRepository.addedOrderListStateFlow.onEach { orderList ->
            addedOrderListStateFlow.value =
                orderList.filter { it.orderEntity.orderStatus != OrderStatus.CANCELED }
                    .toStateSuccess()
        }.launchIn(viewModelScope)
        apiRepository.subscribeOnOrderList(cafeId)
    }

    fun removeOrder(order: Order) {
        apiRepository.delete(order)
    }
}