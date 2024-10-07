package com.bunbeauty.presentation.feature.additionlist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additionlist.GetSeparatedAdditionListUseCase
import com.bunbeauty.domain.feature.additionlist.UpdateVisibleAdditionUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdditionListViewModel @Inject constructor(
    private val getSeparatedAdditionListUseCase: GetSeparatedAdditionListUseCase,
    private val updateVisibleAdditionUseCase: UpdateVisibleAdditionUseCase
) : BaseStateViewModel<AdditionList.DataState, AdditionList.Action, AdditionList.Event>(
    initState = AdditionList.DataState(
        visibleAdditions = listOf(),
        hiddenAdditions = listOf(),
        isLoading = true,
        isRefreshing = false,
        hasError = false
    )
) {

    override fun reduce(action: AdditionList.Action, dataState: AdditionList.DataState) {
        when (action) {
            AdditionList.Action.OnBackClick -> sendEvent { AdditionList.Event.Back }

            is AdditionList.Action.OnAdditionClick -> sendEvent {
                AdditionList.Event.OnAdditionClick(
                    additionUuid = action.additionUuid
                )
            }

             AdditionList.Action.OnCreateAddition -> sendEvent {
                AdditionList.Event.OnCreateAddition
            }

            is AdditionList.Action.OnVisibleClick -> updateVisible(
                uuid = action.uuid,
                isVisible = action.isVisible
            )

            AdditionList.Action.Init -> loadData()

            AdditionList.Action.RefreshData -> refreshData()
        }
    }

    private fun updateVisible(uuid: String, isVisible: Boolean) {
        viewModelScope.launch {
            updateVisibleAdditionUseCase(
                additionUuid = uuid,
                isVisible = !isVisible
            )
            loadData()
        }
    }

    private fun refreshData() {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        isRefreshing = true,
                        hasError = false
                    )
                }

                val separatedAdditionList = getSeparatedAdditionListUseCase(refreshing = true)

                setState {
                    copy(
                        visibleAdditions = separatedAdditionList.visibleList,
                        hiddenAdditions = separatedAdditionList.hiddenList,
                        isLoading = false,
                        isRefreshing = false,
                        hasError = false
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        hasError = true
                    )
                }
            }
        )
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                val separatedAdditionList = getSeparatedAdditionListUseCase(refreshing = false)
                setState {
                    copy(
                        visibleAdditions = separatedAdditionList.visibleList,
                        hiddenAdditions = separatedAdditionList.hiddenList,
                        isLoading = false,
                        isRefreshing = false,
                        hasError = false
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        hasError = true
                    )
                }
            }
        )
    }
}
