package com.bunbeauty.fooddeliveryadmin.coreui

import androidx.compose.runtime.Composable
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

abstract class SingleStateComposeFragment<VDS : BaseViewDataState, A : BaseAction, E : BaseEvent> :
    BaseComposeFragment<VDS, VDS, A, E>() {
    @Composable
    override fun mapState(state: VDS): VDS = state
}
