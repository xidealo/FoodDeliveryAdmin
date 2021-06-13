package com.bunbeauty.fooddeliveryadmin.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.repository.order.OrderRepo
import com.bunbeauty.domain.resources.ResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrderDetailsFragmentArgs
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderDetailsViewModel @Inject constructor(
    private val args: OrderDetailsFragmentArgs,
    private val orderRepo: OrderRepo,
    private val dataStoreHelper: IDataStoreHelper,
    private val resourcesProvider: ResourcesProvider,
) : BaseViewModel() {

    val codeTitle: String
        get() = resourcesProvider.getString(R.string.msg_order_details_order) + args.orderUI.code

    val time: String
        get() = args.orderUI.time

    val pickupMethod: String
        get() {
            return if (args.orderUI.isDelivery) {
                resourcesProvider.getString(R.string.msg_order_delivery)
            } else {
                resourcesProvider.getString(R.string.msg_order_pickup)
            }
        }

    val deferredTime: String
        get() = args.orderUI.deferredTime

    val address: String
        get() = args.orderUI.address

    val comment: String
        get() = args.orderUI.comment

    val oldTotalCost: String
        get() = args.orderUI.oldTotalCost

    val newTotalCost: String
        get() = args.orderUI.newTotalCost

    val productList: String
        get() = args.orderUI
            .cartProductList
            .joinToString { cartProduct ->
                cartProduct.name +
                        resourcesProvider.getString(R.string.msg_colon) +
                        cartProduct.count +
                        resourcesProvider.getString(R.string.msg_break)
            }

    fun changeStatus(order: Order) {
        viewModelScope.launch(IO) {
            order.cafeId = dataStoreHelper.cafeId.first()
            orderRepo.update(order)

            router.navigateUp()
        }
    }
}