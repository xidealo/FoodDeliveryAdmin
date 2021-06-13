package com.bunbeauty.fooddeliveryadmin.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.ExtendedState
import com.bunbeauty.common.State
import com.bunbeauty.fooddeliveryadmin.extensions.toStateAddedSuccess
import com.bunbeauty.fooddeliveryadmin.extensions.toStateUpdatedSuccess
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.model.cart_product.CartProductUI
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.data.model.order.OrderUI
import com.bunbeauty.domain.cost.ICostUtil
import com.bunbeauty.domain.date_time.DateTimeUtil
import com.bunbeauty.domain.product.IProductUtil
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import com.bunbeauty.domain.repository.cafe.CafeRepo
import com.bunbeauty.domain.repository.order.OrderRepo
import com.bunbeauty.domain.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.extensions.toStateSuccess
import com.bunbeauty.fooddeliveryadmin.ui.adapter.OrderItem
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

abstract class OrdersViewModel : BaseViewModel() {

    abstract val cafeAddressStateFlow: StateFlow<State<String>>
    abstract val orderListState: StateFlow<ExtendedState<List<OrderItem>>>

    abstract fun subscribeOnAddress()
    abstract fun subscribeOnOrders()
}

@ExperimentalCoroutinesApi
class OrdersViewModelImpl @Inject constructor(
    private val apiRepository: IApiRepository,
    private val orderRepo: OrderRepo,
    private val cafeRepo: CafeRepo,
    private val stringUtil: IStringUtil,
    private val sstringUtil: com.bunbeauty.domain.string.IStringUtil,
    private val dateTimeUtil: DateTimeUtil,
    private val dataStoreHelper: IDataStoreHelper,
    private val productUtil: IProductUtil,
    private val resourcesProvider: IResourcesProvider,
    private val costUtil: ICostUtil,
) : OrdersViewModel() {

    override val cafeAddressStateFlow = MutableStateFlow<State<String>>(State.Loading())
    get() {
        //subscribeOnAddress()
        return field
    }

    override val orderListState =
        MutableStateFlow<ExtendedState<List<OrderItem>>>(ExtendedState.Loading())
        get() {
            //subscribeOnOrders()
            return field
        }

    override fun subscribeOnAddress() {
        dataStoreHelper.cafeId.flatMapLatest { cafeId ->
            cafeRepo.getCafeByIdFlow(cafeId)
        }.onEach { cafe ->
            orderListState.value = ExtendedState.Loading()

            if (cafe != null) {
                cafeAddressStateFlow.value = stringUtil.toString(cafe.address).toStateSuccess()
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
            val pickupMethod = if (order.orderEntity.isDelivery) {
                resourcesProvider.getString(R.string.msg_order_delivery)
            } else {
                resourcesProvider.getString(R.string.msg_order_pickup)
            }
            val cartProductUIList = order.cartProducts.map { cartProduct ->
                val oldCost = productUtil.getCartProductOldCost(cartProduct)
                val newCost = productUtil.getCartProductNewCost(cartProduct)
                val oldCostString = costUtil.getOldCost(
                    oldCost,
                    newCost,
                    resourcesProvider.getString(R.string.msg_ruble)
                )
                val newCostString =
                    newCost.toString() + resourcesProvider.getString(R.string.msg_ruble)
                CartProductUI(
                    name = productUtil.getPositionName(cartProduct.menuProduct),
                    photoLink = cartProduct.menuProduct.photoLink,
                    count = cartProduct.count.toString() + resourcesProvider.getString(R.string.msg_pieces),
                    oldCost = oldCostString,
                    newCost = newCostString
                )
            }
            val oldCost = productUtil.getOldTotalCost(order.cartProducts)
            val newCost = productUtil.getNewTotalCost(order.cartProducts)
            val oldTotalCostString = costUtil.getOldCost(
                oldCost,
                newCost,
                resourcesProvider.getString(R.string.msg_ruble)
            )
            val newTotalCostString =
                newCost.toString() + resourcesProvider.getString(R.string.msg_ruble)
            OrderItem(
                OrderUI(
                    status = order.orderEntity.orderStatus,
                    code = order.orderEntity.code,
                    deferredTime = sstringUtil.toStringDeferred(order.orderEntity),
                    time = dateTimeUtil.getTimeHHMM(order.timestamp),
                    isDelivery = order.orderEntity.isDelivery,
                    pickupMethod = pickupMethod,
                    comment = order.orderEntity.comment,
                    email = order.orderEntity.email,
                    phone = order.orderEntity.phone,
                    address = stringUtil.toString(order.orderEntity.address),
                    cartProductList = cartProductUIList,
                    oldTotalCost = oldTotalCostString,
                    newTotalCost = newTotalCostString
                )
            )
        }
    }

    fun removeOrder(order: Order) {
        apiRepository.delete(order)
    }
}