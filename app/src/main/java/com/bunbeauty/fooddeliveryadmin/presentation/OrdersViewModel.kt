package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.State
import com.bunbeauty.common.extensions.toStateSuccess
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import com.bunbeauty.domain.repository.cafe.CafeRepo
import com.bunbeauty.domain.string_helper.IStringHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class OrdersViewModel : BaseViewModel() {

    abstract val cafeAddressLiveData: LiveData<String>
    abstract val addedOrderListStateFlow: StateFlow<State<List<Order>>>
    abstract val updatedOrderListStateFlow: StateFlow<State<List<Order>>>

    abstract fun getOrders()
}

class OrdersViewModelImpl @Inject constructor(
    private val apiRepository: IApiRepository,
    private val stringHelper: IStringHelper,
    cafeRepo: CafeRepo,
    private val dataStoreHelper: IDataStoreHelper
) : OrdersViewModel() {

    private val cafeListLiveData = cafeRepo.cafeListFlow.asLiveData()

    override val cafeAddressLiveData: LiveData<String> =
        switchMap(dataStoreHelper.cafeId.asLiveData()) { cafeId ->
            map(cafeListLiveData) { cafeList ->
                val address = cafeList.find { cafe ->
                    cafe.cafeEntity.id == cafeId
                }?.address
                if (address == null) {
                    ""
                } else {
                    stringHelper.toString(address)
                }
            }
        }

    override val addedOrderListStateFlow: MutableStateFlow<State<List<Order>>> =
        MutableStateFlow(State.Loading())

    override val updatedOrderListStateFlow: MutableStateFlow<State<List<Order>>> =
        MutableStateFlow(State.Loading())

    override fun getOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            apiRepository.subscribeOnOrderList(dataStoreHelper.cafeId.first())
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
        }
    }


    fun removeOrder(order: Order) {
        apiRepository.delete(order)
    }
}