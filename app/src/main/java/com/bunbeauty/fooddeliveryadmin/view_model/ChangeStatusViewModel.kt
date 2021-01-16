package com.bunbeauty.fooddeliveryadmin.view_model

import com.bunbeauty.fooddeliveryadmin.data.api.firebase.IApiRepository
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.enums.OrderStatus
import com.bunbeauty.fooddeliveryadmin.ui.orders.ChangeStatusNavigator
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class ChangeStatusViewModel @Inject constructor(
    private val iApiRepository: IApiRepository
) : BaseViewModel<ChangeStatusNavigator>() {

    override var navigator: WeakReference<ChangeStatusNavigator>? = null

    fun cancelClick() {
        navigator?.get()?.closeDialog()
    }

    fun updateClick(){
        navigator?.get()?.updateClick()
    }

    fun changeStatus(order: Order, newStatus: OrderStatus) {
        iApiRepository.updateOrder(order.uuid, newStatus)
    }
}