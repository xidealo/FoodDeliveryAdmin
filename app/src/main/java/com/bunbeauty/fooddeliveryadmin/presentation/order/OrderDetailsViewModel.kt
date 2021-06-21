package com.bunbeauty.fooddeliveryadmin.presentation.order

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.enums.OrderStatus.CANCELED
import com.bunbeauty.domain.model.cart_product.CartProductUI
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.domain.util.order.IOrderUtil
import com.bunbeauty.domain.util.product.IProductUtil
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.CartProductItem
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrderDetailsFragmentArgs
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrderDetailsFragmentDirections.toStatusListBottomSheet
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

abstract class OrderDetailsViewModel : BaseViewModel() {

    abstract val codeTitle: String
    abstract val time: String
    abstract val pickupMethod: String
    abstract val deferredTime: String?
    abstract val address: String
    abstract val comment: String
    abstract var status: String
    abstract val productList: List<CartProductItem>
    abstract val isDelivery: Boolean
    abstract val deliveryCost: String
    abstract val oldOrderCost: String
    abstract val newOrderCost: String

    abstract fun isStatusCanceled(status: String): Boolean
    abstract fun changeStatus(status: String)
    abstract fun goToStatusList()
}

class OrderDetailsViewModelImpl @Inject constructor(
    private val args: OrderDetailsFragmentArgs,
    private val orderRepo: OrderRepo,
    private val productUtil: IProductUtil,
    private val orderUtil: IOrderUtil,
    private val stringUtil: IStringUtil,
    private val dateTimeUtil: IDateTimeUtil,
    dataStoreRepo: DataStoreRepo,
) : OrderDetailsViewModel() {

    private val delivery by lazy {
        runBlocking {
            dataStoreRepo.delivery.first()
        }
    }

    override val codeTitle: String
        get() = stringUtil.getOrderCodeString(args.order.orderEntity.code)

    override val time: String
        get() = dateTimeUtil.getTimeHHMM(args.order.timestamp)

    override val pickupMethod: String
        get() = stringUtil.getReceivingMethodString(args.order.orderEntity.isDelivery)

    override val deferredTime: String?
        get() = args.order.orderEntity.deferred

    override val address: String
        get() = stringUtil.toString(args.order.orderEntity.address)

    override val comment: String
        get() = args.order.orderEntity.comment

    override var status = stringUtil.getOrderStatusString(args.order.orderEntity.orderStatus)

    override val productList: List<CartProductItem>
        get() = args.order.cartProducts.map { cartProduct ->
            val oldCost = productUtil.getCartProductOldCost(cartProduct)
            val newCost = productUtil.getCartProductNewCost(cartProduct)

            CartProductItem(
                CartProductUI(
                    name = productUtil.getPositionName(cartProduct.menuProduct),
                    photoLink = cartProduct.menuProduct.photoLink,
                    count = stringUtil.getProductCountString(cartProduct.count),
                    oldCost = stringUtil.getCostString(oldCost),
                    newCost = stringUtil.getCostString(newCost)
                )
            )
        }

    override val isDelivery: Boolean
        get() = args.order.orderEntity.isDelivery

    override val deliveryCost: String
        get() {
            val deliveryCost = orderUtil.getDeliveryCost(args.order, delivery)
            return stringUtil.getDeliveryString(deliveryCost)
        }

    override val oldOrderCost: String
        get() {
            val oldOrderCost = orderUtil.getOldOrderCost(args.order, delivery)
            return stringUtil.getCostString(oldOrderCost)
        }

    override val newOrderCost: String
        get() {
            val newOrderCost = orderUtil.getNewOrderCost(args.order, delivery)
            return stringUtil.getCostString(newOrderCost)
        }

    override fun isStatusCanceled(status: String): Boolean {
        return stringUtil.getOrderStatusByString(status) == CANCELED
    }

    override fun changeStatus(status: String) {
        viewModelScope.launch(IO) {
            val orderStatus = stringUtil.getOrderStatusByString(status)
            orderRepo.updateStatus(args.order.cafeId, args.order.uuid, orderStatus)

            withContext(Main) {
                router.navigateUp()
            }
        }
    }

    override fun goToStatusList() {
        router.navigate(
            toStatusListBottomSheet(
                args.order.orderEntity.isDelivery,
                !args.order.orderEntity.deferred.isNullOrEmpty()
            )
        )
    }
}