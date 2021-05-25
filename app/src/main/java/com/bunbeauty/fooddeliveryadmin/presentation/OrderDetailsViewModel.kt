package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.model.cart_product.CartProduct
import com.bunbeauty.data.model.cart_product.CartProductUI
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.product.IProductUtil
import com.bunbeauty.domain.repository.order.OrderRepo
import com.bunbeauty.domain.resources.ResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderDetailsViewModel @Inject constructor(
    private val orderRepo: OrderRepo,
    private val dataStoreHelper: IDataStoreHelper,
    private val productUtil: IProductUtil,
    private val resourcesProvider: ResourcesProvider,
) : BaseViewModel() {

    fun getCodeTitle(code: String): String {
        return resourcesProvider.getString(R.string.msg_order_details_order) + code
    }

    fun getPickupMethod(isDelivery: Boolean): String {
        return if (isDelivery) {
            resourcesProvider.getString(R.string.msg_order_delivery)
        } else {
            resourcesProvider.getString(R.string.msg_order_pickup)
        }
    }

    fun getProductsList(cartProductList: List<CartProductUI>): String {
        return cartProductList.joinToString { cartProduct ->
            cartProduct.name +
                    resourcesProvider.getString(R.string.msg_colon) +
                    cartProduct.count +
                    resourcesProvider.getString(R.string.msg_break)
        }
    }

    fun changeStatus(order: Order) {
        viewModelScope.launch(IO) {
            order.cafeId = dataStoreHelper.cafeId.first()
            orderRepo.update(order)

            router.navigateUp()
        }
    }
}