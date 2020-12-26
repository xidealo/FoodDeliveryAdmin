package com.bunbeauty.fooddeliveryadmin.view_model

import com.bunbeauty.fooddeliveryadmin.ui.log_in.LoginNavigator
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class LoginViewModel @Inject constructor(): BaseViewModel<LoginNavigator>() {
    override var navigator: WeakReference<LoginNavigator>? = null

}