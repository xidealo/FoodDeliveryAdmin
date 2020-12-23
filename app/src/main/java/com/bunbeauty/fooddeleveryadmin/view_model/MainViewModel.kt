package com.bunbeauty.fooddeleveryadmin.view_model

import com.bunbeauty.fooddeleveryadmin.ui.main.MainNavigator
import com.bunbeauty.fooddeleveryadmin.view_model.base.BaseViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseViewModel<MainNavigator>() {

    override var navigator: WeakReference<MainNavigator>? = null

}