package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface SelectAdditionGroup {
    data class DataState(
        val selectableAdditionGroupList: List<SelectableAdditionGroupItem>,
        val state: State,
    ) : BaseViewDataState {

        data class SelectableAdditionGroupItem(
            val uuid: String,
            val name: String,
            val isSelected: Boolean
        )

        enum class State {
            LOADING,
            ERROR,
            SUCCESS
        }
    }

    sealed interface Action : BaseAction {
        data class Init(
            val selectedAdditionGroupUuid: String?,
        ) : Action

        data object OnBackClick : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
    }
}
