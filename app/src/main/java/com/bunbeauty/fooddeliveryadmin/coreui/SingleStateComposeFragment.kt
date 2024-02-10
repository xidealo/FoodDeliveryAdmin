package com.bunbeauty.fooddeliveryadmin.coreui

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

abstract class SingleStateComposeFragment<VDS : BaseViewDataState, A : BaseAction, E : BaseEvent> :
    BaseComposeFragment<VDS, VDS, A, E>() {

    override fun mapState(state: VDS): VDS {
        return state
    }
}
