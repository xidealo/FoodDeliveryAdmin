package com.bunbeauty.fooddeliveryadmin.presentation.order

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.model.cart_product.CartProductUI
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.product.IProductUtil
import com.bunbeauty.domain.repository.order.OrderRepo
import com.bunbeauty.fooddeliveryadmin.presentation.BaseViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.CartProductItem
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrderDetailsFragmentArgs
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrderDetailsFragmentDirections.toStatusListBottomSheet
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class OrderDetailsViewModel : BaseViewModel() {

    abstract val codeTitle: String
    abstract val time: String
    abstract val pickupMethod: String
    abstract val deferredTime: String
    abstract val address: String
    abstract val comment: String
    abstract val status: String
    abstract val productList: List<CartProductItem>
    abstract val oldTotalCost: String
    abstract val newTotalCost: String

    abstract fun changeStatus(order: Order)
    abstract fun goToStatusList()
}

class OrderDetailsViewModelImpl @Inject constructor(
    private val args: OrderDetailsFragmentArgs,
    private val orderRepo: OrderRepo,
    private val dataStoreHelper: IDataStoreHelper,
    private val productUtil: IProductUtil,
    private val stringUtil: IStringUtil,
) : OrderDetailsViewModel() {

    override val codeTitle: String
        get() = stringUtil.getOrderCodeString(args.orderUI.code)

    override val time: String
        get() = args.orderUI.time

    override val pickupMethod: String
        get() = stringUtil.getReceivingMethodString(args.orderUI.isDelivery)

    override val deferredTime: String
        get() = args.orderUI.deferredTime

    override val address: String
        get() = args.orderUI.address

    override val comment: String
        get() = args.orderUI.comment

    override var status = stringUtil.getOrderStatusString(args.orderUI.status)

    override val productList: List<CartProductItem>
        get() = args.orderUI.cartProductList.map { cartProduct ->
            val oldCost = productUtil.getCartProductOldCost(cartProduct)
            val newCost = productUtil.getCartProductNewCost(cartProduct)

            CartProductItem(
                CartProductUI(
                    name = productUtil.getPositionName(cartProduct.menuProduct),
                    photoLink = cartProduct.menuProduct.photoLink,
                    count = stringUtil.getCountString(cartProduct.count),
                    oldCost = stringUtil.getCostString(oldCost),
                    newCost = stringUtil.getCostString(newCost)
                )
            )
        }

    override val oldTotalCost: String
        get() = args.orderUI.oldTotalCost

    override val newTotalCost: String
        get() = args.orderUI.newTotalCost

    override fun changeStatus(order: Order) {
        viewModelScope.launch(IO) {
            order.cafeId = dataStoreHelper.cafeId.first()
            orderRepo.update(order)

            router.navigateUp()
        }
    }

    override fun goToStatusList() {
        router.navigate(
            toStatusListBottomSheet(
                args.orderUI.isDelivery,
                args.orderUI.deferredTime.isNotEmpty()
            )
        )
    }
}