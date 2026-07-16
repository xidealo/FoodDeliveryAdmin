package com.bunbeauty.shared.feature.statisticuser

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.clientuser.GetClientUserListUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel

private const val PAGE_SIZE = 20

class StatisticUserViewModel(
    private val getClientUserListUseCase: GetClientUserListUseCase,
) : BaseStateViewModel<StatisticUser.DataState, StatisticUser.Action, StatisticUser.Event>(
        initState =
            StatisticUser.DataState(
                state = StatisticUser.DataState.State.LOADING,
                users = emptyList(),
                offset = 0,
                total = 0,
                isPageLoading = false,
                canLoadMore = false,
                isSearchEnabled = false,
                searchQuery = "",
            ),
    ) {
    override fun reduce(
        action: StatisticUser.Action,
        dataState: StatisticUser.DataState,
    ) {
        when (action) {
            StatisticUser.Action.Init -> handleInit(dataState)
            StatisticUser.Action.LoadMore -> handleLoadMore(dataState)
            StatisticUser.Action.SearchClick -> handleSearchClick()
            is StatisticUser.Action.SearchQueryChange -> handleSearchQueryChange(action.searchQuery)
            is StatisticUser.Action.UserClick -> handleUserClick(action.userUuid)
            StatisticUser.Action.BackClick -> handleBackClick()
        }
    }

    private fun handleInit(dataState: StatisticUser.DataState) {
        if (dataState.users.isNotEmpty()) {
            return
        }
        loadFirstPage()
    }

    private fun loadFirstPage() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        state = StatisticUser.DataState.State.LOADING,
                        isPageLoading = true,
                    )
                }
                val page =
                    getClientUserListUseCase(
                        limit = PAGE_SIZE,
                        offset = 0,
                    )
                setState {
                    copy(
                        state = StatisticUser.DataState.State.SUCCESS,
                        users = page.results,
                        offset = page.results.size,
                        total = page.count,
                        isPageLoading = false,
                        canLoadMore = page.results.size < page.count,
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = StatisticUser.DataState.State.ERROR,
                        isPageLoading = false,
                    )
                }
            },
        )
    }

    private fun handleLoadMore(dataState: StatisticUser.DataState) {
        if (dataState.isPageLoading || !dataState.canLoadMore || dataState.isSearchEnabled) {
            return
        }
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(isPageLoading = true)
                }
                val page =
                    getClientUserListUseCase(
                        limit = PAGE_SIZE,
                        offset = dataState.offset,
                    )
                setState {
                    val updatedUsers = users + page.results
                    copy(
                        users = updatedUsers,
                        offset = updatedUsers.size,
                        total = page.count,
                        isPageLoading = false,
                        canLoadMore = updatedUsers.size < page.count,
                    )
                }
            },
            onError = {
                setState {
                    copy(isPageLoading = false)
                }
            },
        )
    }

    private fun handleSearchClick() {
        setState {
            val newSearchEnabled = !isSearchEnabled
            copy(
                isSearchEnabled = newSearchEnabled,
                searchQuery =
                    if (newSearchEnabled) {
                        searchQuery
                    } else {
                        ""
                    },
            )
        }
    }

    private fun handleSearchQueryChange(searchQuery: String) {
        setState {
            copy(searchQuery = searchQuery)
        }
    }

    private fun handleUserClick(userUuid: String) {
        sendEvent {
            StatisticUser.Event.OpenUserDetails(userUuid = userUuid)
        }
    }

    private fun handleBackClick() {
        sendEvent {
            StatisticUser.Event.GoBack
        }
    }
}
