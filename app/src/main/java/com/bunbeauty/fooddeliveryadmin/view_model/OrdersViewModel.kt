package com.bunbeauty.fooddeliveryadmin.view_model

import androidx.databinding.ObservableField
import com.bunbeauty.fooddeliveryadmin.data.api.firebase.IApiRepository
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersNavigator
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class OrdersViewModel @Inject constructor(
    private val apiRepository: IApiRepository
) : BaseViewModel<OrdersNavigator>() {

    override var navigator: WeakReference<OrdersNavigator>? = null
    val addOrderWithCartProducts = ObservableField<Order?>()
    val addOrderWithCartProductList = ObservableField<List<Order>>()
    val replaceOrderWithCartProducts = ObservableField<Order?>()
    val addedOrderListLiveData = apiRepository.addedOrderListLiveData
    val updatedOrderListLiveData = apiRepository.updatedOrderListLiveData

}