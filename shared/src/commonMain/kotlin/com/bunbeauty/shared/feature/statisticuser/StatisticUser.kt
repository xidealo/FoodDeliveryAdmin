package com.bunbeauty.shared.feature.statisticuser

import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettings
import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent

interface StatisticUser {
    data class DataState(
        val state: State,
        val users: List<ClientUserSettings>,
        val offset: Int,
        val total: Int,
        val isPageLoading: Boolean,
        val canLoadMore: Boolean,
        val isSearchEnabled: Boolean,
        val searchQuery: String,
    ) : BaseDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR,
        }
    }

    sealed interface Action : BaseAction {
        data object Init : Action

        data object LoadMore : Action

        data object SearchClick : Action

        data class SearchQueryChange(
            val searchQuery: String,
        ) : Action

        data class UserClick(
            val userUuid: String,
        ) : Action

        data object BackClick : Action
    }

    sealed interface Event : BaseEvent {
        data class OpenUserDetails(
            val userUuid: String,
        ) : Event

        data object GoBack : Event
    }
}
