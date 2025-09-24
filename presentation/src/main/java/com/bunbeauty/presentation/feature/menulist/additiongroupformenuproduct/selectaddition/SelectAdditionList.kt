package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface SelectAdditionList {
    data class DataState(
        val state: State,
        val groupName: String
    ) : BaseViewDataState {

        enum class State {
            LOADING,
            ERROR,
            SUCCESS
        }
    }

    sealed interface Action : BaseAction {
        data class Init(
            val menuProductUuid: String,
            val additionGroupUuid: String?
        ) : Action

        data object OnBackClick : Action
        data class SelectAdditionClick(val uuid: String) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
    }
}
