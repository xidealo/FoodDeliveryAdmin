package com.bunbeauty.fooddeliveryadmin.coreui

import com.bunbeauty.presentation.viewmodel.base.Action
import com.bunbeauty.presentation.viewmodel.base.Event
import com.bunbeauty.presentation.viewmodel.base.ViewDataState

abstract class SingleStateComposeFragment<VDS : ViewDataState, A : Action, E : Event> :
    BaseComposeFragment<VDS, VDS, A, E>() {

    override fun mapState(state: VDS): VDS {
        return state
    }

}