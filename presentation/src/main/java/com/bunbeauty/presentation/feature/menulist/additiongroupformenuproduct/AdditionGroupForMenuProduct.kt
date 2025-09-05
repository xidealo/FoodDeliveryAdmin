package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct

import com.bunbeauty.domain.model.additiongroup.AdditionGroupWithAdditions
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface AdditionGroupForMenuProduct {
    data class DataState(
        val additionGroupList: List<AdditionGroupWithAdditions>
    ) : BaseDataState

    sealed interface Action : BaseAction {
        data class Init(val menuProductUuid: String) : Action
        data object OnBackClick : Action
        data class OnAdditionGroupClick(val uuid: String) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        data class OnAdditionGroupClick(val uuid: String) : Event
    }
}
