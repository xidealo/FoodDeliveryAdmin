package com.bunbeauty.fooddeliveryadmin.presentation.order

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.enums.OrderStatus.CANCELED
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.domain.util.order.IOrderUtil
import com.bunbeauty.domain.util.product.IProductUtil
import com.bunbeauty.fooddeliveryadmin.extensions.navArgs
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.items.CartProductItem
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrderDetailsFragmentArgs
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrderDetailsFragmentDirections.toStatusListBottomSheet
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
    private val dateTimeUtil: IDateTimeUtil,
    dataStoreRepo: DataStoreRepo,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val args: OrderDetailsFragmentArgs by savedStateHandle.navArgs()
    private val delivery by lazy {
        runBlocking {
            dataStoreRepo.delivery.first()
        }
    }

    val codeTitle: String
        get() = stringUtil.getOrderCodeString(args.order.code)

    val time: String
        get() = dateTimeUtil.getTimeHHMM(args.order.time)

    val pickupMethod: String
        get() = stringUtil.getReceivingMethodString(args.order.delivery)

    val deferredTime: String?
        get() = args.order.deferred

    val address: String?
        get() = stringUtil.getUserAddressString(args.order.address)

    val comment: String
        get() = args.order.comment ?: ""

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

    val isDelivery: Boolean
        get() = args.order.delivery

    val deliveryCost: String
        get() {
            val deliveryCost = orderUtil.getDeliveryCost(args.order, delivery)
            return stringUtil.getDeliveryString(deliveryCost)
        }

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
            toStatusListBottomSheet(
                args.order.delivery,
                !args.order.deferred.isNullOrEmpty()
            )
        )
    }
}