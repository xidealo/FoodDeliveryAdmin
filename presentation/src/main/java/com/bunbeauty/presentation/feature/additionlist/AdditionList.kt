package com.bunbeauty.presentation.feature.additionlist

import com.bunbeauty.domain.model.addition.Addition
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface AdditionList {
    data class DataState(
        val visibleAdditions: List<AdditionFeedItem>,
        val hiddenAdditions: List<AdditionFeedItem>,
        val isLoading: Boolean,
        val isRefreshing: Boolean,
        val hasError: Boolean
    ) : BaseDataState {

        sealed interface AdditionFeedItem {
            data class Title(val title: String?, val key: String) : AdditionFeedItem
            data class AdditionItem(val addition: Addition) : AdditionFeedItem
        }
    }

    sealed interface Action : BaseAction {

        data object Init : Action
        data object RefreshData : Action
        data class OnAdditionClick(val additionUuid: String) : Action
        data class OnVisibleClick(val isVisible: Boolean, val uuid: String) : Action
        data object OnBackClick : Action
    }

    sealed interface Event : BaseEvent {
        data class OnAdditionClick(val additionUuid: String) : Event
        data object Back : Event
    }
}
