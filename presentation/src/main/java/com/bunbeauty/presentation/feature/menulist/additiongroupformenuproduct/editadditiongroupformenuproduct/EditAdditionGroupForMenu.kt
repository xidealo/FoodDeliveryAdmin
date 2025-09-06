package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface EditAdditionGroupForMenu {
    data class DataState(
        val additionUuid: String,
        val groupName: String,
        val state: State,
        val additionNameList: String?,
        val isVisible: Boolean,
    ) : BaseViewDataState {
        enum class State {
            Loading,
            Error,
            Success
        }
    }

    sealed interface Action : BaseAction {
        data class Init(
            val additionGroupForMenuUuid: String,
            val menuProductUuid: String,
        ) : Action

        data object OnBackClick : Action
        data class OnAdditionGroupClick(val uuid: String) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        data class OnAdditionGroupClick(val uuid: String) : Event
    }
}
