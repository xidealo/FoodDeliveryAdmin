package com.bunbeauty.presentation.feature.additionlist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additionlist.GetSeparatedAdditionListUseCase
import com.bunbeauty.domain.feature.additionlist.UpdateVisibleAdditionUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.model.MenuListEvent
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdditionListViewModel @Inject constructor(
    private val getSeparatedAdditionListUseCase: GetSeparatedAdditionListUseCase,
    private val updateVisibleAdditionUseCase: UpdateVisibleAdditionUseCase
) : BaseStateViewModel<AdditionList.ViewDataState, AdditionList.Action, AdditionList.Event>(
    initState = AdditionList.ViewDataState(
        visibleAdditions = listOf(),
        hiddenAdditions = listOf(),
        isLoading = false,
        isRefreshing = false,
        throwable = null
    )
) {

    override fun reduce(action: AdditionList.Action, dataState: AdditionList.ViewDataState) {
        when (action) {
            AdditionList.Action.OnBackClick -> addEvent { AdditionList.Event.Back }

            is AdditionList.Action.OnAdditionClick -> addEvent {
                AdditionList.Event.OnAdditionClick(
                    additionUuid = action.additionUuid
                )
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
        viewModelScope.launch() {
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
                        throwable = null
                    )
                }

                val separatedAdditionList = getSeparatedAdditionListUseCase()

                setState {
                    copy(
                        visibleAdditions = separatedAdditionList.visibleList,
                        hiddenAdditions = separatedAdditionList.hiddenList,
                        isLoading = false,
                        isRefreshing = false,
                        throwable = null
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        throwable = throwable
                    )
                }
            }
        )
    }

    private fun loadData() {
        viewModelScope.launchSafe(
            block = {
                val separatedAdditionList = getSeparatedAdditionListUseCase()
                setState {
                    copy(
                        visibleAdditions = separatedAdditionList.visibleList,
                        hiddenAdditions = separatedAdditionList.hiddenList,
                        isLoading = false,
                        isRefreshing = false,
                        throwable = null
                    )
                }
            },
            onError = { throwable ->
                setState {
                    copy(
                        throwable = throwable
                    )
                }
            }
        )
    }
}
