package com.bunbeauty.presentation.feature.additiongrouplist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additiongrouplist.GetSeparatedAdditionGroupListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdditionGroupListViewModel @Inject constructor(
    private val getSeparatedAdditionGroupListUseCase: GetSeparatedAdditionGroupListUseCase
) : BaseStateViewModel<AdditionGroupList.ViewDataState, AdditionGroupList.Action, AdditionGroupList.Event>(
    initState = AdditionGroupList.ViewDataState(
        visibleAdditionGroups = listOf(),
        hiddenAdditionGroups = listOf(),
        isLoading = false,
        isRefreshing = false
    )
) {

    override fun reduce(
        action: AdditionGroupList.Action,
        dataState: AdditionGroupList.ViewDataState
    ) {
        when (action) {
            AdditionGroupList.Action.OnBackClick -> {
                addEvent { AdditionGroupList.Event.Back }
            }

            AdditionGroupList.Action.OnAdditionClick -> {
            }

            is AdditionGroupList.Action.OnVisibleClick -> updateVisible()

            AdditionGroupList.Action.Init -> loadData()
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
            onError = {
                // show error
            }
        )
    }

    private fun updateVisible() {
    }
}
