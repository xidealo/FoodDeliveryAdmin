package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface CreateAdditionGroupForMenu {
    data class DataState(
        val additionGroupForMenuProductUuid: String,
        val editedAdditionGroupUuid: String?,
        val groupName: String?,
        val state: State,
        val additionNameList: String?,
        val editedAdditionListUuid: List<String>?,
        val isVisible: Boolean,
        val menuProductUuid: String
    ) : BaseViewDataState {

        enum class State {
            LOADING,
            SUCCESS,
            ERROR
        }
    }

    sealed interface Action : BaseAction {
        data class SelectAdditionGroup(val additionGroupUuid: String) : Action
        data class SelectAdditionList(val additionListUuid: List<String>) : Action
        data object OnAdditionGroupClick : Action
        data object OnAdditionListClick : Action
        data object OnSaveClick : Action
        data object OnBackClick : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        data object SaveAndBack : Event
        data class OnAdditionGroupClick(val uuid: String) : Event
        data class OnAdditionListClick(
            val additionGroupUuid: String,
            val menuProductUuid: String,
            val additionGroupName: String
        ) : Event
    }
}
