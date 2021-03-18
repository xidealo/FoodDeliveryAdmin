package com.bunbeauty.presentation.view_model

import com.bunbeauty.presentation.navigator.SelectedStatisticNavigator
import java.lang.ref.WeakReference
import javax.inject.Inject

class SelectedStatisticViewModel @Inject constructor() :
    BaseViewModel<SelectedStatisticNavigator>() {
    override var navigator: WeakReference<SelectedStatisticNavigator>? = null

}