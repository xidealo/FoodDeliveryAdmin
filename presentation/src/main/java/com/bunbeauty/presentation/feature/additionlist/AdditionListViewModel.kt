package com.bunbeauty.presentation.feature.additionlist

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additionlist.GetSeparatedAdditionListUseCase
import com.bunbeauty.domain.feature.additionlist.UpdateVisibleAdditionUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.model.MenuProductItem
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdditionListViewModel @Inject constructor(
    private val getSeparatedAdditionListUseCase: GetSeparatedAdditionListUseCase,
    private val updateVisibleAdditionUseCase: UpdateVisibleAdditionUseCase,
) : BaseStateViewModel<AdditionList.ViewDataState, AdditionList.Action, AdditionList.Event>(
    initState = AdditionList.ViewDataState(
        visibleAdditions = listOf(),
        hiddenAdditions = listOf(),
        isLoading = false,
        isRefreshing = false
    )
) {

    override fun reduce(action: AdditionList.Action, dataState: AdditionList.ViewDataState) {
        when (action) {
            AdditionList.Action.OnBackClick -> {
                addEvent { AdditionList.Event.Back }
            }

            AdditionList.Action.OnAdditionClick -> {

            }

            is AdditionList.Action.OnVisibleClick -> {
                updateVisible(uuid = action.uuid, isVisible = action.isVisible)
            }

            AdditionList.Action.Init -> loadData()
        }
    }


    fun updateVisible(uuid: String, isVisible: Boolean) {
        viewModelScope.launch() {
            updateVisibleAdditionUseCase(
                additionUuid = uuid,
                isVisible = !isVisible
            )
            loadData()
        }
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
                        isRefreshing = false
                    )
                }
            },
            onError = {
                //show error
            }
        )
    }


}
