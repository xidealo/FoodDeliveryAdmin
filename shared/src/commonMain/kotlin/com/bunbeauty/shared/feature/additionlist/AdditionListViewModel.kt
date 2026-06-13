package com.bunbeauty.shared.feature.additionlist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additionlist.GetSeparatedAdditionListUseCase
import com.bunbeauty.domain.feature.additionlist.UpdateVisibleAdditionUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel

class AdditionListViewModel(
    private val getSeparatedAdditionListUseCase: GetSeparatedAdditionListUseCase,
    private val updateVisibleAdditionUseCase: UpdateVisibleAdditionUseCase,
) : BaseStateViewModel<AdditionList.DataState, AdditionList.Action, AdditionList.Event>(
        initState =
            AdditionList.DataState(
                visibleAdditions = listOf(),
                hiddenAdditions = listOf(),
                isLoading = true,
                isRefreshing = false,
                hasError = false,
            ),
    ) {
    override fun reduce(
        action: AdditionList.Action,
        dataState: AdditionList.DataState,
    ) {
        when (action) {
            AdditionList.Action.OnBackClick -> sendEvent { AdditionList.Event.Back }

            is AdditionList.Action.OnAdditionClick ->
                sendEvent {
                    AdditionList.Event.OnAdditionClick(
                        additionUuid = action.additionUuid,
                    )
                }

            is AdditionList.Action.OnVisibleClick ->
                updateVisible(
                    uuid = action.uuid,
                    isVisible = action.isVisible,
                )

            AdditionList.Action.Init -> loadData()

            AdditionList.Action.RefreshData -> refreshData()

            AdditionList.Action.OnSearchClicked -> onSearchClicked(dataState)

            is AdditionList.Action.OnSearchQueryChange -> onSearchQueryChange(action.searchQuery)
        }
    }

    private fun onSearchClicked(dataState: AdditionList.DataState) {
        val isSearchEnabled = !dataState.isSearchEnabled
        setState {
            copy(
                isSearchEnabled = isSearchEnabled,
                searchQuery =
                    if (isSearchEnabled) {
                        searchQuery
                    } else {
                        ""
                    },
            )
        }
    }

    private fun onSearchQueryChange(searchQuery: String) {
        setState {
            copy(searchQuery = searchQuery)
        }
    }

    private fun updateVisible(
        uuid: String,
        isVisible: Boolean,
    ) {
        viewModelScope.launchSafe(
            block = {
                updateVisibleAdditionUseCase(
                    additionUuid = uuid,
                    isVisible = !isVisible,
                )
                loadData()
            },
            onError = {
                showErrorState()
            },
        )
    }

    private fun refreshData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        isRefreshing = true,
                        hasError = false,
                    )
                }

                val separatedAdditionList = getSeparatedAdditionListUseCase(refreshing = true)
                setState {
                    copy(
                        visibleAdditions =
                            separatedAdditionList.visibleList
                                .flatMap { groupedAdditionList ->
                                    groupedAdditionList.toAdditionFeedItemList()
                                },
                        hiddenAdditions =
                            separatedAdditionList.hiddenList
                                .flatMap { groupedAdditionList ->
                                    groupedAdditionList.toAdditionFeedItemList()
                                },
                        isLoading = false,
                        isRefreshing = false,
                        hasError = false,
                    )
                }
            },
            onError = {
                showErrorState()
            },
        )
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                val separatedAdditionList = getSeparatedAdditionListUseCase(refreshing = false)
                setState {
                    copy(
                        visibleAdditions =
                            separatedAdditionList.visibleList
                                .flatMap { groupedAdditionList ->
                                    groupedAdditionList.toAdditionFeedItemList()
                                },
                        hiddenAdditions =
                            separatedAdditionList.hiddenList
                                .flatMap { groupedAdditionList ->
                                    groupedAdditionList.toAdditionFeedItemList()
                                },
                        isLoading = false,
                        isRefreshing = false,
                        hasError = false,
                    )
                }
            },
            onError = {
                showErrorState()
            },
        )
    }

    private fun showErrorState() {
        setState {
            copy(
                hasError = true,
                isRefreshing = false,
                isLoading = false,
            )
        }
    }
}
