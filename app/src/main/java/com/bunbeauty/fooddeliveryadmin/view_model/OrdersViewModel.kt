package com.bunbeauty.fooddeliveryadmin.view_model

import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.Transformations
import com.bunbeauty.fooddeliveryadmin.data.api.firebase.IApiRepository
import com.bunbeauty.fooddeliveryadmin.data.model.order.OrderWithCartProducts
import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersNavigator
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class OrdersViewModel @Inject constructor(
        private val apiRepository: IApiRepository
) : BaseViewModel<OrdersNavigator>() {

    override var navigator: WeakReference<OrdersNavigator>? = null
    val addOrderWithCartProducts = ObservableField<OrderWithCartProducts?>()
    val addOrderWithCartProductList = ObservableField<List<OrderWithCartProducts>>()
    val replaceOrderWithCartProducts = ObservableField<OrderWithCartProducts?>()
    private val orderWithCartProductsList = arrayListOf<String>()

    val orderWithCartProductsListLiveData by lazy {
        Transformations.map(apiRepository.getOrderWithCartProductsList()) { ordersList ->
            orderWithCartProductsList.addAll(ordersList.map { it.uuid })
            addOrderWithCartProductList.set(ordersList)
        }
    }

    val isNewOrderLiveData by lazy {
        /*Transformations.map(apiRepository.getOrderWithCartProducts()) { orderWithCartProducts ->
            val foundOrderWithCartProducts =
                orderWithCartProductsList.find { it == orderWithCartProducts.uuid }
            if (foundOrderWithCartProducts == null) {
                orderWithCartProductsList.add(orderWithCartProducts.uuid)
                addOrderWithCartProducts.set(orderWithCartProducts)
                //createNotification
                true
            } else {
                replaceOrderWithCartProducts.set(orderWithCartProducts)
                false
            }
        }*/
    }

    val orderListLiveData = apiRepository.orderListLiveData

}