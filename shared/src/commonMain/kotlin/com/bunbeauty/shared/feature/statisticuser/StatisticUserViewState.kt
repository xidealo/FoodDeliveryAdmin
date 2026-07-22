package com.bunbeauty.shared.feature.statisticuser

import androidx.compose.runtime.Immutable
import com.bunbeauty.domain.feature.clientuser.model.ClientUserSettings
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class StatisticUserViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val users: ImmutableList<UserItem>,
            val isSearchEnabled: Boolean,
            val searchQuery: String,
            val searchResultList: ImmutableList<UserItem>?,
            val canLoadMore: Boolean,
            val isPageLoading: Boolean,
            val searchCanLoadMore: Boolean,
            val isSearchLoading: Boolean,
        ) : State
    }

    @Immutable
    data class UserItem(
        val uuid: String,
        val phoneNumber: String,
    )
}

internal fun StatisticUser.DataState.toViewState(): StatisticUserViewState =
    StatisticUserViewState(
        state =
            when (state) {
                StatisticUser.DataState.State.LOADING -> StatisticUserViewState.State.Loading
                StatisticUser.DataState.State.ERROR -> StatisticUserViewState.State.Error
                StatisticUser.DataState.State.SUCCESS ->
                    StatisticUserViewState.State.Success(
                        users =
                            users
                                .map { user -> user.toItem() }
                                .toPersistentList(),
                        isSearchEnabled = isSearchEnabled,
                        searchQuery = searchQuery,
                        searchResultList = getSearchResultList(),
                        canLoadMore = canLoadMore,
                        isPageLoading = isPageLoading,
                        searchCanLoadMore = searchCanLoadMore,
                        isSearchLoading = isSearchLoading,
                    )
            },
    )

private fun StatisticUser.DataState.getSearchResultList(): ImmutableList<StatisticUserViewState.UserItem>? {
    if (!isSearchEnabled) {
        return null
    }

    return searchUsers
        ?.map { user -> user.toItem() }
        ?.toPersistentList()
}

private fun ClientUserSettings.toItem(): StatisticUserViewState.UserItem =
    StatisticUserViewState.UserItem(
        uuid = uuid,
        phoneNumber = phoneNumber,
    )
