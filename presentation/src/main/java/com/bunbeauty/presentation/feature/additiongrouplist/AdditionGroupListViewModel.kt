package com.bunbeauty.presentation.feature.additiongrouplist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additiongrouplist.GetSeparatedAdditionGroupListUseCase
import com.bunbeauty.domain.feature.additiongrouplist.UpdateVisibleAdditionGroupListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.feature.category.CategoryListState
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class AdditionGroupListViewModel(
    private val getSeparatedAdditionGroupListUseCase: GetSeparatedAdditionGroupListUseCase,
    private val updateVisibleAdditionGroupListUseCase: UpdateVisibleAdditionGroupListUseCase
) : BaseStateViewModel<AdditionGroupList.DataState, AdditionGroupList.Action, AdditionGroupList.Event>(
    initState = AdditionGroupList.DataState(
        visibleAdditionGroups = listOf(),
        hiddenAdditionGroups = listOf(),
        isLoading = true,
        isRefreshing = false,
        error = null
    )
) {

    override fun reduce(
        action: AdditionGroupList.Action,
        dataState: AdditionGroupList.DataState
    ) {
        when (action) {
            AdditionGroupList.Action.OnBackClick -> {
                sendEvent { AdditionGroupList.Event.Back }
            }

            is AdditionGroupList.Action.OnAdditionClick -> additionGroupClick(action.additionUuid)

            is AdditionGroupList.Action.OnVisibleClick -> updateVisible(
                uuid = action.uuid,
                isVisible = action.isVisible
            )

            AdditionGroupList.Action.Init -> loadData()

            AdditionGroupList.Action.RefreshData -> refreshData()
        }
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                val separatedAdditionList = getSeparatedAdditionGroupListUseCase(refreshing = false)
                setState {
                    copy(
                        visibleAdditionGroups = separatedAdditionList.visibleList,
                        hiddenAdditionGroups = separatedAdditionList.hiddenList,
                        isLoading = false,
                        isRefreshing = false
                    )
                }
            },
            onError = { throwable ->
                setState {
                    copy(
                        error = throwable
                    )
                }
            }
        )
    }

    private fun updateVisible(uuid: String, isVisible: Boolean) {
        viewModelScope.launchSafe(
            block = {
                updateVisibleAdditionGroupListUseCase(
                    additionUuidGroup = uuid,
                    isVisible = !isVisible
                )
                loadData()
            },
            onError = { throwable ->
                setState {
                    copy(
                        error = throwable
                    )
                }
            }
        )
    }

    private fun refreshData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        isRefreshing = true,
                        error = null
                    )
                }

                val separatedAdditionGroupList = getSeparatedAdditionGroupListUseCase(refreshing = true)

                setState {
                    copy(
                        visibleAdditionGroups = separatedAdditionGroupList.visibleList,
                        hiddenAdditionGroups = separatedAdditionGroupList.hiddenList,
                        isLoading = false,
                        isRefreshing = false,
                        error = null
                    )
                }
            },
            onError = { throwable ->
                setState {
                    copy(
                        error = throwable
                    )
                }
            }
        )
    }

    private fun additionGroupClick(additionUuid: String) {
        sendEvent {
            AdditionGroupList.Event.OnAdditionGroupClick(additionUuid = additionUuid)
        }
    }
}
