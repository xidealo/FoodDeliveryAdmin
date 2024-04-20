package com.bunbeauty.presentation.feature.additiongrouplist

import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface AdditionGroupList {
    data class ViewDataState(
        val visibleAdditionGroups: List<AdditionGroup>,
        val hiddenAdditionGroups: List<AdditionGroup>,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
    ) : BaseViewDataState

    sealed interface Action : BaseAction {

        data object Init : Action
        data object OnAdditionClick : Action
        data class OnVisibleClick(val isVisible: Boolean, val uuid: String) : Action
        data object OnBackClick : Action
    }

    sealed interface Event : BaseEvent {
        data class OnAdditionClick(val additionUuid: String) : Event
        data object Back : Event
    }
}