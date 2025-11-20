package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectadditiongroup

import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectadditiongroup.SelectableAdditionGroup
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface SelectAdditionGroup {
    data class DataState(
        val visibleSelectableAdditionGroupList: List<SelectableAdditionGroup>,
        val hiddenSelectableAdditionGroupList: List<SelectableAdditionGroup>,
        val state: State,
    ) : BaseViewDataState {
        enum class State {
            LOADING,
            ERROR,
            SUCCESS,
        }
    }

    sealed interface Action : BaseAction {
        data class Init(
            val selectedAdditionGroupUuid: String?,
            val menuProductUuid: String,
            val mainEditedAdditionGroupUuid: String?,
        ) : Action

        data object OnBackClick : Action

        data class SelectAdditionGroupClick(
            val uuid: String,
            val name: String,
        ) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event

        data class SelectAdditionGroupClicked(
            val additionGroupUuid: String,
            val additionGroupName: String,
        ) : Event
    }
}
