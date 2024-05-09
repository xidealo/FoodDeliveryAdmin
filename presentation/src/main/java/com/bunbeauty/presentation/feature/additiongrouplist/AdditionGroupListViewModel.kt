package com.bunbeauty.presentation.feature.additiongrouplist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additiongrouplist.GetSeparatedAdditionGroupListUseCase
import com.bunbeauty.domain.feature.additiongrouplist.UpdateVisibleAdditionGroupUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdditionGroupListViewModel @Inject constructor(
    private val getSeparatedAdditionGroupListUseCase: GetSeparatedAdditionGroupListUseCase,
    private val updateVisibleAdditionGroupListUseCase: UpdateVisibleAdditionGroupUseCase
) : BaseStateViewModel<AdditionGroupList.DataState, AdditionGroupList.Action, AdditionGroupList.Event>(
    initState = AdditionGroupList.DataState(
        visibleAdditionGroups = listOf(),
        hiddenAdditionGroups = listOf(),
        isLoading = false,
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
                addEvent { AdditionGroupList.Event.Back }
            }

            AdditionGroupList.Action.OnAdditionClick -> {
                // TODO (implement)
            }

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
                val separatedAdditionList = getSeparatedAdditionGroupListUseCase()
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

                val separatedAdditionGroupList = getSeparatedAdditionGroupListUseCase()

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

    private fun refreshData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        isRefreshing = true,
                        throwable = null
                    )
                }

                val separatedAdditionGroupList = getSeparatedAdditionGroupListUseCase()

                setState {
                    copy(
                        visibleAdditionGroups = separatedAdditionGroupList.visibleList,
                        hiddenAdditionGroups = separatedAdditionGroupList.hiddenList,
                        isLoading = false,
                        isRefreshing = false,
                        throwable = null
                    )
                }
            },
            onError =
            {
                setState {
                    copy(
                        throwable = throwable
                    )
                }
            }
        )
    }
}
