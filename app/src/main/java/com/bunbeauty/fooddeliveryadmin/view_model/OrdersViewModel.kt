package com.bunbeauty.fooddeliveryadmin.view_model

import androidx.databinding.ObservableField
import androidx.lifecycle.Transformations
import com.bunbeauty.fooddeliveryadmin.data.api.firebase.IApiRepository
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderWithCartProducts
import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersNavigator
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class OrdersViewModel @Inject constructor(
    private val iApiRepository: IApiRepository
) : BaseViewModel<OrdersNavigator>() {

    val addOrderWithCartProducts = ObservableField<OrderWithCartProducts?>()
    val addOrderWithCartProductList = ObservableField<List<OrderWithCartProducts>>()
    val replaceOrderWithCartProducts = ObservableField<OrderWithCartProducts?>()
    private val orderWithCartProductsList = arrayListOf<String>()

    val orderWithCartProductsListLiveData by lazy {
        Transformations.map(iApiRepository.getOrderWithCartProductsList()) { ordersList ->
            orderWithCartProductsList.addAll(ordersList.map { it.uuid })
            addOrderWithCartProductList.set(ordersList)
        }
    }

    val orderWithCartProductsLiveData by lazy {
        Transformations.map(iApiRepository.getOrderWithCartProducts()) { orderWithCartProducts ->
            val foundOrderWithCartProducts =
                orderWithCartProductsList.find { it == orderWithCartProducts.uuid }
            if (foundOrderWithCartProducts == null) {
                orderWithCartProductsList.add(orderWithCartProducts.uuid)
                addOrderWithCartProducts.set(orderWithCartProducts)
                //createNotification
                //go to top
                true
            } else {
                replaceOrderWithCartProducts.set(orderWithCartProducts)
                false
            }
        }
    }
    override var navigator: WeakReference<OrdersNavigator>? = null

}