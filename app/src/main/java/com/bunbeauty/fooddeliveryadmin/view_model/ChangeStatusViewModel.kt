package com.bunbeauty.fooddeliveryadmin.view_model

import com.bunbeauty.fooddeliveryadmin.ui.orders.ChangeStatusNavigator
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class ChangeStatusViewModel @Inject constructor() : BaseViewModel<ChangeStatusNavigator>() {

    override var navigator: WeakReference<ChangeStatusNavigator>? = null

    fun cancelClick() {
        navigator?.get()?.closeDialog()
    }
}