package com.bunbeauty.presentation.feature.additionlist

import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface AdditionList {
    data class ViewDataState(
        val visibleAdditions: List<Addition>,
        val hiddenAdditions: List<Addition>,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
        val throwable: Throwable?
    ) : BaseViewDataState

    sealed interface Action : BaseAction {

        data object Init : Action
        data object RefreshData : Action
        data class OnAdditionClick(val additionUuid: String)  : Action
        data class OnVisibleClick(val isVisible: Boolean, val uuid: String) : Action
        data object OnBackClick : Action
    }

    sealed interface Event : BaseEvent {
        data class OnAdditionClick(val additionUuid: String) : Event
        data object Back : Event
    }

}