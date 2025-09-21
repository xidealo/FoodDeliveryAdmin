package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface EditAdditionGroupForMenu {
    data class DataState(
        val additionGroupForMenuProductUuid: String,
        val editedAdditionGroupUuid: String? = null,
        val groupName: String,
        val state: State,
        val additionNameList: String?,
        val isVisible: Boolean
    ) : BaseViewDataState {
        enum class State {
            LOADING,
            ERROR,
            SUCCESS
        }
    }

    sealed interface Action : BaseAction {
        data class Init(
            val additionGroupForMenuUuid: String,
            val menuProductUuid: String
        ) : Action

        data object OnBackClick : Action
        data class OnAdditionGroupClick(val uuid: String) : Action
        data object OnSaveClick : Action
        data class SelectAdditionGroup(val additionGroupUuid: String) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        data class OnAdditionGroupClick(val uuid: String) : Event
    }
}
