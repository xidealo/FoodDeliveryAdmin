package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.editadditiongroupformenuproduct

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface EditAdditionGroupForMenu {
    data class DataState(
        val groupName: String,
        val loading: Boolean,
        val additionNameList: String,
        val isVisible: Boolean,
    ) : BaseViewDataState

    sealed interface Action : BaseAction {
        data class Init(val additionGroupForMenuUuid: String) : Action
        data object OnBackClick : Action
        data class OnAdditionGroupClick(val uuid: String) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        data class OnAdditionGroupClick(val uuid: String) : Event
    }
}
