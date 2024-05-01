package com.bunbeauty.presentation.feature.menu

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface Menu {
    data object ViewDataState : BaseViewDataState

    sealed interface Action : BaseAction {

        data object OnMenuListClick : Action
        data object OnAdditionGroupsListClick : Action
        data object OnAdditionsListClick : Action
    }

    sealed interface Event : BaseEvent {
        data object OnMenuListClick : Event
        data object OnAdditionGroupsListClick : Event
        data object OnAdditionsListClick : Event
    }
}
