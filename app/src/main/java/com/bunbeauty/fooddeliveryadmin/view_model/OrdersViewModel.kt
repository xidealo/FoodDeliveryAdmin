package com.bunbeauty.fooddeliveryadmin.view_model

import com.bunbeauty.fooddeliveryadmin.ui.orders.OrdersNavigator
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class OrdersViewModel @Inject constructor() : BaseViewModel<OrdersNavigator>() {

    override var navigator: WeakReference<OrdersNavigator>? = null
}