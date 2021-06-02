package com.bunbeauty.fooddeliveryadmin.presentation.order

import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.CAFE_ADDRESS_REQUEST_KEY
import com.bunbeauty.common.Constants.LIST_ARGS_KEY
import com.bunbeauty.common.Constants.ORDER_ARGS_KEY
import com.bunbeauty.common.Constants.REQUEST_KEY_ARGS_KEY
import com.bunbeauty.common.Constants.SELECTED_CAFE_ADDRESS_KEY
import com.bunbeauty.common.Constants.SELECTED_KEY_ARGS_KEY
import com.bunbeauty.common.Constants.TITLE_ARGS_KEY
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.extensions.toStateAddedSuccess
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.fooddeliveryadmin.extensions.toStateUpdatedSuccess
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.state.ExtendedState
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.ui.items.OrderItem
import com.bunbeauty.fooddeliveryadmin.ui.items.list.CafeAddress
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderRepo: OrderRepo,
    private val cafeRepo: CafeRepo,
    private val stringUtil: IStringUtil,
    private val dateTimeUtil: IDateTimeUtil,
    private val resourcesProvider: IResourcesProvider,
    private val dataStoreRepo: DataStoreRepo,
) : BaseViewModel() {

    private val mutableAddressState: MutableStateFlow<State<String>> =
        MutableStateFlow(State.Loading())
    val addressState: StateFlow<State<String>> = mutableAddressState.asStateFlow()

    private val mutableOrderListState: MutableStateFlow<ExtendedState<List<OrderItem>>> =
        MutableStateFlow(ExtendedState.Loading())
    val orderListState: StateFlow<ExtendedState<List<OrderItem>>> =
        mutableOrderListState.asStateFlow()

    init {
        subscribeOnAddress()
        subscribeOnOrders()
    }

    fun saveSelectedCafeAddress(cafeAddress: CafeAddress) {
        viewModelScope.launch {
            dataStoreRepo.saveCafeUuid(cafeAddress.cafeUuid)
        }
    }

    fun goToAddressList() {
        viewModelScope.launch {
            val addressList = cafeRepo.getCafeList().map { cafe ->
                CafeAddress(title = cafe.address, cafeUuid = cafe.uuid)
            }.toTypedArray()

            withContext(Main) {
                router.navigate(
                    R.id.to_listBottomSheet,
                    bundleOf(
                        TITLE_ARGS_KEY to resourcesProvider.getString(R.string.title_orders_select_cafe),
                        LIST_ARGS_KEY to addressList,
                        SELECTED_KEY_ARGS_KEY to SELECTED_CAFE_ADDRESS_KEY,
                        REQUEST_KEY_ARGS_KEY to CAFE_ADDRESS_REQUEST_KEY,
                    )
                )
            }
        }
    }

    fun goToOrderDetails(order: Order) {
        router.navigate(
            R.id.to_OrdersDetailsFragment,
            bundleOf(ORDER_ARGS_KEY to order)
        )
    }

    private fun subscribeOnAddress() {
        dataStoreRepo.cafeUuid.flatMapLatest { cafeId ->
            cafeRepo.getCafeByUuid(cafeId)
        }.onEach { cafe ->
            if (cafe != null) {
                mutableAddressState.value = cafe.address.toStateSuccess()
            }
        }.launchIn(viewModelScope)
    }

    private fun subscribeOnOrders() {
        dataStoreRepo.cafeUuid.flatMapLatest { cafeId ->
            orderRepo.getAddedOrderListByCafeId(cafeId)
        }.onEach { orderList ->
            mutableOrderListState.value = toOrderItemList(orderList).toStateAddedSuccess()
        }.launchIn(viewModelScope)

        dataStoreRepo.cafeUuid.flatMapLatest { cafeId ->
            orderRepo.getUpdatedOrderListByCafeId(cafeId)
        }.onEach { orderList ->
            mutableOrderListState.value = toOrderItemList(orderList).toStateUpdatedSuccess()
        }.launchIn(viewModelScope)
    }

    private fun toOrderItemList(orderList: List<Order>): List<OrderItem> {
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
}