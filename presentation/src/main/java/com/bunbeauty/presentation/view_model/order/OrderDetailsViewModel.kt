package com.bunbeauty.presentation.view_model.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.ORDER_ARGS_KEY
import com.bunbeauty.common.Constants.SELECTED_STATUS_KEY
import com.bunbeauty.common.Constants.STATUS_REQUEST_KEY
import com.bunbeauty.domain.enums.OrderStatus.*
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.domain.util.order.IOrderUtil
import com.bunbeauty.domain.util.product.IProductUtil
import com.bunbeauty.presentation.utils.IResourcesProvider
import com.bunbeauty.presentation.model.ListData
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.extension.navArg
import com.bunbeauty.presentation.model.list.OrderStatus
import com.bunbeauty.presentation.model.CartProductItemModel
import com.bunbeauty.presentation.navigation_event.OrderDetailsNavigationEvent
import com.bunbeauty.presentation.utils.IStringUtil
import com.bunbeauty.presentation.view_model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val orderRepo: OrderRepo,
    private val productUtil: IProductUtil,
    private val orderUtil: IOrderUtil,
    private val stringUtil: IStringUtil,
    private val resourcesProvider: IResourcesProvider,
    dateTimeUtil: IDateTimeUtil,
    dataStoreRepo: DataStoreRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val order: Order = savedStateHandle.navArg(ORDER_ARGS_KEY)!!

    private val delivery by lazy {
        runBlocking {
            dataStoreRepo.delivery.first()
        }
    }

    val codeTitle: String = stringUtil.getOrderCodeString(order.code)

    val phone: String = order.phone

    val time: String = dateTimeUtil.getTimeHHMM(order.time)

    val pickupMethod: String = stringUtil.getReceivingMethodString(order.delivery)

    val deferredTime: String? = order.deferred

    val address: String? = stringUtil.getUserAddressString(order.address)

    val comment: String = order.comment ?: ""

    var status = stringUtil.getOrderStatusString(order.orderStatus)

    val productList: List<CartProductItemModel> = order.cartProductList
        .map { cartProduct ->
            val oldCost = productUtil.getCartProductOldCost(cartProduct)
            val newCost = productUtil.getCartProductNewCost(cartProduct)

            CartProductItemModel(
                name = productUtil.getPositionName(cartProduct.menuProduct),
                photoLink = cartProduct.menuProduct.photoLink,
                count = stringUtil.getProductCountString(cartProduct.count),
                oldCost = stringUtil.getCostString(oldCost),
                newCost = stringUtil.getCostString(newCost)
            )
        }

    val isDelivery: Boolean = order.delivery

    val deliveryCost: String
        get() {
            val deliveryCost = orderUtil.getDeliveryCost(order, delivery)
            return stringUtil.getDeliveryString(deliveryCost)
        }

    val bonuses: String = stringUtil.getBonusString(order.bonus)

    val oldOrderCost: String
        get() {
            val oldOrderCost = orderUtil.getOldOrderCost(order, delivery)
            return stringUtil.getCostString(oldOrderCost)
        }

    val newOrderCost: String
        get() {
            val newOrderCost = orderUtil.getNewOrderCost(order, delivery)
            return stringUtil.getCostString(newOrderCost)
        }

    private val orderStatusList: List<OrderStatus>
        get() {
            val isDeferred = !order.deferred.isNullOrEmpty()
            return when {
                isDeferred && isDelivery -> {
                    listOf(
                        OrderStatus(stringUtil.getOrderStatusString(NOT_ACCEPTED)),
                        OrderStatus(stringUtil.getOrderStatusString(ACCEPTED)),
                        OrderStatus(stringUtil.getOrderStatusString(PREPARING)),
                        OrderStatus(stringUtil.getOrderStatusString(SENT_OUT)),
                        OrderStatus(stringUtil.getOrderStatusString(DELIVERED)),
                        OrderStatus(stringUtil.getOrderStatusString(CANCELED)),
                    )
                }
                isDeferred && !isDelivery -> {
                    listOf(
                        OrderStatus(stringUtil.getOrderStatusString(NOT_ACCEPTED)),
                        OrderStatus(stringUtil.getOrderStatusString(ACCEPTED)),
                        OrderStatus(stringUtil.getOrderStatusString(PREPARING)),
                        OrderStatus(stringUtil.getOrderStatusString(DONE)),
                        OrderStatus(stringUtil.getOrderStatusString(DELIVERED)),
                        OrderStatus(stringUtil.getOrderStatusString(CANCELED)),
                    )
                }
                !isDeferred && isDelivery -> {
                    listOf(
                        OrderStatus(stringUtil.getOrderStatusString(NOT_ACCEPTED)),
                        OrderStatus(stringUtil.getOrderStatusString(PREPARING)),
                        OrderStatus(stringUtil.getOrderStatusString(SENT_OUT)),
                        OrderStatus(stringUtil.getOrderStatusString(DELIVERED)),
                        OrderStatus(stringUtil.getOrderStatusString(CANCELED)),
                    )
                }
                !isDeferred && !isDelivery -> {
                    listOf(
                        OrderStatus(stringUtil.getOrderStatusString(NOT_ACCEPTED)),
                        OrderStatus(stringUtil.getOrderStatusString(PREPARING)),
                        OrderStatus(stringUtil.getOrderStatusString(DONE)),
                        OrderStatus(stringUtil.getOrderStatusString(DELIVERED)),
                        OrderStatus(stringUtil.getOrderStatusString(CANCELED)),
                    )
                }
                else -> emptyList()
            }
        }

    fun isStatusCanceled(status: String): Boolean {
        return stringUtil.getOrderStatusByString(status) == CANCELED
    }

    fun changeStatus(status: String) {
        viewModelScope.launch(IO) {
            val orderStatus = stringUtil.getOrderStatusByString(status)
            orderRepo.updateStatus(order.cafeUuid, order.uuid, orderStatus)

            goBack()
        }
    }

    fun goToStatusList() {
        val listData = ListData(
            title = resourcesProvider.getString(R.string.title_order_status),
            listItem = orderStatusList,
            requestKey = STATUS_REQUEST_KEY,
            selectedKey = SELECTED_STATUS_KEY
        )
        goTo(OrderDetailsNavigationEvent.ToStatusList(listData))
    }
}