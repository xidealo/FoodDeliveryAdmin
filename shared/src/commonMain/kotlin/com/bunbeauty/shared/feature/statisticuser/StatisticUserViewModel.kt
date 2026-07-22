package com.bunbeauty.shared.feature.statisticuser

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.clientuser.GetClientUserListUseCase
import com.bunbeauty.domain.feature.clientuser.GetClientUserSearchUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val PAGE_SIZE = 20
private const val MIN_SEARCH_LENGTH = 2
private const val SEARCH_DEBOUNCE_MS = 50L

class StatisticUserViewModel(
    private val getClientUserListUseCase: GetClientUserListUseCase,
    private val getClientUserSearchUseCase: GetClientUserSearchUseCase,
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
                searchUsers = null,
                searchOffset = 0,
                searchTotal = 0,
                isSearchLoading = false,
                searchCanLoadMore = false,
            ),
    ) {
    private val searchQueryFlow = MutableStateFlow("")

    init {
        observeSearchQuery()
    }

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

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(SEARCH_DEBOUNCE_MS)
                .map { query -> query.trim() }
                .distinctUntilChanged()
                .collect { query ->
                    if (query.length >= MIN_SEARCH_LENGTH) {
                        searchFirstPage(query)
                    } else {
                        clearSearchResults()
                    }
                }
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
        if (dataState.isSearchEnabled && dataState.searchUsers != null) {
            loadMoreSearch(dataState)
        } else {
            loadMoreBrowse(dataState)
        }
    }

    private fun loadMoreBrowse(dataState: StatisticUser.DataState) {
        if (dataState.isPageLoading || !dataState.canLoadMore) {
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

    private fun loadMoreSearch(dataState: StatisticUser.DataState) {
        if (dataState.isSearchLoading || !dataState.searchCanLoadMore) {
            return
        }
        val query = dataState.searchQuery.trim()
        if (query.length < MIN_SEARCH_LENGTH) {
            return
        }
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(isSearchLoading = true)
                }
                val page =
                    getClientUserSearchUseCase(
                        query = query,
                        limit = PAGE_SIZE,
                        offset = dataState.searchOffset,
                    )
                setState {
                    val updatedUsers = (searchUsers ?: emptyList()) + page.results
                    copy(
                        searchUsers = updatedUsers,
                        searchOffset = updatedUsers.size,
                        searchTotal = page.count,
                        isSearchLoading = false,
                        searchCanLoadMore = updatedUsers.size < page.count,
                    )
                }
            },
            onError = {
                setState {
                    copy(isSearchLoading = false)
                }
            },
        )
    }

    private fun searchFirstPage(query: String) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(isSearchLoading = true)
                }
                val page =
                    getClientUserSearchUseCase(
                        query = query,
                        limit = PAGE_SIZE,
                        offset = 0,
                    )
                setState {
                    copy(
                        searchUsers = page.results,
                        searchOffset = page.results.size,
                        searchTotal = page.count,
                        isSearchLoading = false,
                        searchCanLoadMore = page.results.size < page.count,
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        isSearchLoading = false,
                        searchUsers = emptyList(),
                        searchCanLoadMore = false,
                    )
                }
            },
        )
    }

    private fun clearSearchResults() {
        setState {
            copy(
                searchUsers = null,
                searchOffset = 0,
                searchTotal = 0,
                isSearchLoading = false,
                searchCanLoadMore = false,
            )
        }
    }

    private fun handleSearchClick() {
        setState {
            val newSearchEnabled = !isSearchEnabled
            copy(
                isSearchEnabled = newSearchEnabled,
                searchQuery = "",
                searchUsers = null,
                searchOffset = 0,
                searchTotal = 0,
                isSearchLoading = false,
                searchCanLoadMore = false,
            )
        }
        searchQueryFlow.value = ""
    }

    private fun handleSearchQueryChange(searchQuery: String) {
        setState {
            copy(searchQuery = searchQuery)
        }
        searchQueryFlow.value = searchQuery
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
