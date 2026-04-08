package com.bunbeauty.shared.feature.additiongrouplist

import com.bunbeauty.domain.model.additiongroup.AdditionGroup
import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent

interface AdditionGroupList {
    data class DataState(
        val visibleAdditionGroups: List<AdditionGroup>,
        val hiddenAdditionGroups: List<AdditionGroup>,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
        val error: Throwable?,
    ) : BaseDataState

    sealed interface Action : BaseAction {
        data object Init : Action

        data object RefreshData : Action

        data class OnAdditionClick(
            val additionUuid: String,
        ) : Action

        data class OnVisibleClick(
            val isVisible: Boolean,
            val uuid: String,
        ) : Action

        data object OnBackClick : Action
    }

    sealed interface Event : BaseEvent {
        data class OnAdditionGroupClick(
            val additionUuid: String,
        ) : Event

        data object Back : Event
    }
}
