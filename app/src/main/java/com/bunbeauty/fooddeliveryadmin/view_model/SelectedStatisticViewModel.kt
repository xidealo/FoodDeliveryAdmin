package com.bunbeauty.fooddeliveryadmin.view_model

import com.bunbeauty.fooddeliveryadmin.ui.statistic.SelectedStatisticNavigator
import com.bunbeauty.fooddeliveryadmin.view_model.base.BaseViewModel
import java.lang.ref.WeakReference
import javax.inject.Inject

class SelectedStatisticViewModel @Inject constructor() :
    BaseViewModel<SelectedStatisticNavigator>() {
    override var navigator: WeakReference<SelectedStatisticNavigator>? = null

}