package com.bunbeauty.fooddeliveryadmin.presentation.order

import androidx.lifecycle.viewModelScope
import com.bunbeauty.data.enums.OrderStatus.CANCELED
import com.bunbeauty.data.model.cart_product.CartProductUI
import com.bunbeauty.domain.date_time.IDateTimeUtil
import com.bunbeauty.domain.product.IProductUtil
import com.bunbeauty.domain.repository.order.OrderRepo
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.CartProductItem
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrderDetailsFragmentArgs
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrderDetailsFragmentDirections.toStatusListBottomSheet
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
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
    abstract val oldTotalCost: String
    abstract val newTotalCost: String

    abstract fun isStatusCanceled(status: String): Boolean
    abstract fun changeStatus(status: String)
    abstract fun goToStatusList()
}

class OrderDetailsViewModelImpl @Inject constructor(
    private val args: OrderDetailsFragmentArgs,
    private val orderRepo: OrderRepo,
    private val productUtil: IProductUtil,
    private val stringUtil: IStringUtil,
    private val dateTimeUtil: IDateTimeUtil,
) : OrderDetailsViewModel() {

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

    override val oldTotalCost: String
        get() {
            val oldTotalCost = productUtil.getOldTotalCost(args.order.cartProducts)
            return stringUtil.getCostString(oldTotalCost)
        }

    override val newTotalCost: String
        get() {
            val newTotalCost = productUtil.getNewTotalCost(args.order.cartProducts)
            return stringUtil.getCostString(newTotalCost)
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