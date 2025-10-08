package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface EditAdditionGroupForMenu {
    data class DataState(
        val additionGroupForMenuProductUuid: String,
        val editedAdditionGroupUuid: String?,
        val groupName: String,
        val state: State,
        val additionNameList: String?,
        val editedAdditionListUuid: List<String>?,
        val isVisible: Boolean,
        val menuProductUuid: String
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
        data class OnAdditionListClick(val uuid: String) : Action
        data object OnSaveClick : Action
        data class SelectAdditionGroup(val additionGroupUuid: String) : Action
        data class SelectAdditionList(val additionListUuid: List<String>) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        data class OnAdditionGroupClick(val uuid: String) : Event
        data class OnAdditionListClick(
            val additionGroupUuid: String,
            val menuProductUuid: String,
            val additionGroupName: String,
        ) : Event
    }
}
