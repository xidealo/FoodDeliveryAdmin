package com.bunbeauty.presentation.view_model.order

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.CAFE_ADDRESS_REQUEST_KEY
import com.bunbeauty.common.Constants.SELECTED_CAFE_ADDRESS_KEY
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.DeliveryRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.extension.toStateAddedSuccess
import com.bunbeauty.presentation.extension.toStateSuccess
import com.bunbeauty.presentation.model.list.CafeAddress
import com.bunbeauty.presentation.model.ListData
import com.bunbeauty.presentation.model.OrderItemModel
import com.bunbeauty.presentation.navigation_event.OrdersNavigationEvent
import com.bunbeauty.presentation.state.ExtendedState
import com.bunbeauty.presentation.state.State
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderRepo: OrderRepo,
    private val cafeRepo: CafeRepo,
    private val resourcesProvider: IResourcesProvider,
    private val dataStoreRepo: DataStoreRepo,
    private val stringUtil: IStringUtil,
    private val dateTimeUtil: IDateTimeUtil,
    private val deliveryRepo: DeliveryRepo,
) : BaseViewModel() {

    private val mutableAddressState: MutableStateFlow<State<String>> =
        MutableStateFlow(State.Loading())
    val addressState: StateFlow<State<String>> = mutableAddressState.asStateFlow()

    private val mutableOrderListState: MutableStateFlow<ExtendedState<List<OrderItemModel>>> =
        MutableStateFlow(ExtendedState.Loading())
    val orderListState: StateFlow<ExtendedState<List<OrderItemModel>>> =
        mutableOrderListState.asStateFlow()

    init {
        subscribeOnAddress()
        subscribeOnOrders()
        viewModelScope.launch(Default) {
            cafeRepo.refreshCafeList(
                token = dataStoreRepo.token.first(),
                cityUuid = dataStoreRepo.managerCity.first()
            )
            deliveryRepo.refreshDelivery(
                token = dataStoreRepo.token.first(),
                cityUuid = dataStoreRepo.managerCity.first()
            )
        }
    }

    fun saveSelectedCafeAddress(cafeAddress: CafeAddress) {
        viewModelScope.launch(Default) {
            cafeAddress.cafeUuid?.let { cafeUuid ->
                //unsubscribe previous cafe from socket and firebase
                if (cafeUuid != dataStoreRepo.cafeUuid.first())
                    orderRepo.unsubscribeOnOrderList(dataStoreRepo.cafeUuid.first())

                dataStoreRepo.saveCafeUuid(cafeUuid)
            }
        }
    }

    fun goToAddressList() {
        viewModelScope.launch(Default) {
            val addressList = cafeRepo.getCafeList().map { cafe ->
                CafeAddress(title = cafe.address, cafeUuid = cafe.uuid)
            }

            val listData = ListData(
                title = resourcesProvider.getString(R.string.title_orders_select_cafe),
                listItem = addressList,
                requestKey = CAFE_ADDRESS_REQUEST_KEY,
                selectedKey = SELECTED_CAFE_ADDRESS_KEY
            )
            goTo(OrdersNavigationEvent.ToCafeAddressList(listData))
        }
    }

    fun goToOrderDetails(orderItemModel: OrderItemModel) {
        goTo(OrdersNavigationEvent.ToOrderDetails(orderItemModel.order))
    }

    private fun subscribeOnAddress() {
        dataStoreRepo.cafeUuid.flatMapLatest { cafeId ->
            cafeRepo.getCafeByUuid(cafeId)
        }.onEach { cafe ->
            if (cafe != null) {
                mutableAddressState.value = cafe.address.toStateSuccess()
            } else {
                mutableAddressState.value = State.Empty()
            }
        }.launchIn(viewModelScope)
    }

    private fun subscribeOnOrders() {
        dataStoreRepo.cafeUuid.flatMapLatest { cafeId ->
            dataStoreRepo.token.onEach { token ->
                if (cafeId.isNotEmpty()) {
                    mutableOrderListState.value = ExtendedState.Loading()
                    orderRepo.loadOrderListByCafeId(token, cafeId)
                    orderRepo.subscribeOnOrderListByCafeId(token, cafeId)
                } else {
                    mutableOrderListState.value = ExtendedState.Empty()
                }
            }
        }.launchIn(viewModelScope)

        orderRepo.ordersMapFlow.onEach { list ->
            mutableOrderListState.value =
                list.map(::toItemModel).filter { it.status != OrderStatus.CANCELED }
                    .toStateAddedSuccess()
        }.launchIn(viewModelScope)
    }

    private fun toItemModel(order: Order): OrderItemModel {
        return OrderItemModel(
            status = order.orderStatus,
            statusString = stringUtil.getOrderStatusString(order.orderStatus),
            code = order.code,
            deferredTime = stringUtil.getDeferredTimeString(order.deferred) ?: "",
            time = dateTimeUtil.getDateTimeDDMMHHMM(order.time),
            order = order
        )
    }
}