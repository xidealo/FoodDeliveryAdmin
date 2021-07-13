package com.bunbeauty.presentation.view_model.order

import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.Constants.LIST_ARGS_KEY
import com.bunbeauty.common.Constants.REQUEST_KEY_ARGS_KEY
import com.bunbeauty.common.Constants.SELECTED_KEY_ARGS_KEY
import com.bunbeauty.common.Constants.SELECTED_STATUS_KEY
import com.bunbeauty.common.Constants.STATUS_REQUEST_KEY
import com.bunbeauty.common.Constants.TITLE_ARGS_KEY
import com.bunbeauty.domain.enums.OrderStatus.*
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.domain.util.order.IOrderUtil
import com.bunbeauty.domain.util.product.IProductUtil
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.extensions.navArgs
import com.bunbeauty.presentation.view_model.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrderDetailsFragmentArgs
import com.bunbeauty.fooddeliveryadmin.ui.items.CartProductItem
import com.bunbeauty.fooddeliveryadmin.ui.items.list.OrderStatus
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
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

    private val args: OrderDetailsFragmentArgs by savedStateHandle.navArgs()
    private val delivery by lazy {
        runBlocking {
            dataStoreRepo.delivery.first()
        }
    }

    val codeTitle: String = stringUtil.getOrderCodeString(args.order.code)

    val phone: String = args.order.phone

    val time: String = dateTimeUtil.getTimeHHMM(args.order.time)

    val pickupMethod: String = stringUtil.getReceivingMethodString(args.order.delivery)

    val deferredTime: String? = args.order.deferred

    val address: String? = stringUtil.getUserAddressString(args.order.address)

    val comment: String = args.order.comment ?: ""

    var status = stringUtil.getOrderStatusString(args.order.orderStatus)

    val productList: List<CartProductItem>
        get() = args.order.cartProductList.map { cartProduct ->
            val oldCost = productUtil.getCartProductOldCost(cartProduct)
            val newCost = productUtil.getCartProductNewCost(cartProduct)

            CartProductItem(
                name = productUtil.getPositionName(cartProduct.menuProduct),
                photoLink = cartProduct.menuProduct.photoLink,
                count = stringUtil.getProductCountString(cartProduct.count),
                oldCost = stringUtil.getCostString(oldCost),
                newCost = stringUtil.getCostString(newCost)
            )
        }

    val isDelivery: Boolean = args.order.delivery

    val deliveryCost: String
        get() {
            val deliveryCost = orderUtil.getDeliveryCost(args.order, delivery)
            return stringUtil.getDeliveryString(deliveryCost)
        }

    val bonuses: String = stringUtil.getBonusString(args.order.bonus)

    val oldOrderCost: String
        get() {
            val oldOrderCost = orderUtil.getOldOrderCost(args.order, delivery)
            return stringUtil.getCostString(oldOrderCost)
        }

    val newOrderCost: String
        get() {
            val newOrderCost = orderUtil.getNewOrderCost(args.order, delivery)
            return stringUtil.getCostString(newOrderCost)
        }

    private val orderStatusArray: Array<OrderStatus>
        get() {
            val isDeferred = !args.order.deferred.isNullOrEmpty()
            return when {
                isDeferred && isDelivery -> {
                    arrayOf(
                        OrderStatus(stringUtil.getOrderStatusString(NOT_ACCEPTED)),
                        OrderStatus(stringUtil.getOrderStatusString(ACCEPTED)),
                        OrderStatus(stringUtil.getOrderStatusString(PREPARING)),
                        OrderStatus(stringUtil.getOrderStatusString(SENT_OUT)),
                        OrderStatus(stringUtil.getOrderStatusString(DELIVERED)),
                        OrderStatus(stringUtil.getOrderStatusString(CANCELED)),
                    )
                }
                isDeferred && !isDelivery -> {
                    arrayOf(
                        OrderStatus(stringUtil.getOrderStatusString(NOT_ACCEPTED)),
                        OrderStatus(stringUtil.getOrderStatusString(ACCEPTED)),
                        OrderStatus(stringUtil.getOrderStatusString(PREPARING)),
                        OrderStatus(stringUtil.getOrderStatusString(DONE)),
                        OrderStatus(stringUtil.getOrderStatusString(DELIVERED)),
                        OrderStatus(stringUtil.getOrderStatusString(CANCELED)),
                    )
                }
                !isDeferred && isDelivery -> {
                    arrayOf(
                        OrderStatus(stringUtil.getOrderStatusString(NOT_ACCEPTED)),
                        OrderStatus(stringUtil.getOrderStatusString(PREPARING)),
                        OrderStatus(stringUtil.getOrderStatusString(SENT_OUT)),
                        OrderStatus(stringUtil.getOrderStatusString(DELIVERED)),
                        OrderStatus(stringUtil.getOrderStatusString(CANCELED)),
                    )
                }
                !isDeferred && !isDelivery -> {
                    arrayOf(
                        OrderStatus(stringUtil.getOrderStatusString(NOT_ACCEPTED)),
                        OrderStatus(stringUtil.getOrderStatusString(PREPARING)),
                        OrderStatus(stringUtil.getOrderStatusString(DONE)),
                        OrderStatus(stringUtil.getOrderStatusString(DELIVERED)),
                        OrderStatus(stringUtil.getOrderStatusString(CANCELED)),
                    )
                }
                else -> emptyArray()
            }
        }

    fun isStatusCanceled(status: String): Boolean {
        return stringUtil.getOrderStatusByString(status) == CANCELED
    }

    fun changeStatus(status: String) {
        viewModelScope.launch(IO) {
            val orderStatus = stringUtil.getOrderStatusByString(status)
            orderRepo.updateStatus(args.order.cafeUuid, args.order.uuid, orderStatus)

            withContext(Main) {
                router.navigateUp()
            }
        }
    }

    fun goToStatusList() {
        router.navigate(
            R.id.to_listBottomSheet,
            bundleOf(
                TITLE_ARGS_KEY to resourcesProvider.getString(R.string.title_order_details_status),
                LIST_ARGS_KEY to orderStatusArray,
                SELECTED_KEY_ARGS_KEY to SELECTED_STATUS_KEY,
                REQUEST_KEY_ARGS_KEY to STATUS_REQUEST_KEY,
            )
        )
    }
}