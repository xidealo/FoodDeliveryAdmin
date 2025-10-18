package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.selectaddition

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.selectaddition.GetSelectedAdditionListUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class SelectAdditionListViewModel(
    val getSelectedAdditionListUseCase: GetSelectedAdditionListUseCase
) :
    BaseStateViewModel<SelectAdditionList.DataState, SelectAdditionList.Action, SelectAdditionList.Event>(
        initState = SelectAdditionList.DataState(
            state = SelectAdditionList.DataState.State.LOADING,
            groupName = "",
            selectedAdditionList = emptyList(),
            notSelectedAdditionList = emptyList()
        )
    ) {

    override fun reduce(
        action: SelectAdditionList.Action,
        dataState: SelectAdditionList.DataState
    ) {
        when (action) {
            is SelectAdditionList.Action.Init -> loadData(
                selectedGroupAdditionUuid = action.additionGroupUuid,
                menuProductUuid = action.menuProductUuid,
                selectedGroupAdditionName = action.additionGroupName
            )

            SelectAdditionList.Action.OnBackClick -> backClick()
            is SelectAdditionList.Action.SelectAdditionClick -> selectAddition(uuid = action.uuid)
            is SelectAdditionList.Action.RemoveAdditionClick -> removeAddition(uuid = action.uuid)
            SelectAdditionList.Action.SelectAdditionListClick -> selectAdditionListClick(
                additionUuidList = dataState.selectedAdditionList.map { additionItem ->
                    additionItem.uuid
                }
            )
        }
    }

    fun selectAddition(uuid: String) {
        setState {
            val commonList = notSelectedAdditionList + selectedAdditionList
            val addition =
                commonList.find { additionItem ->
                    additionItem.uuid == uuid
                } ?: return

            copy(
                notSelectedAdditionList = notSelectedAdditionList.toMutableList()
                    .apply {
                        remove(element = addition)
                    },
                selectedAdditionList = selectedAdditionList.toMutableList()
                    .apply {
                        add(addition)
                    }
            )
        }
    }

    fun removeAddition(uuid: String) {
        setState {
            val commonList = notSelectedAdditionList + selectedAdditionList
            val addition =
                commonList.find { additionItem ->
                    additionItem.uuid == uuid
                } ?: return

            copy(
                notSelectedAdditionList = notSelectedAdditionList.toMutableList()
                    .apply {
                        add(element = addition)
                    },
                selectedAdditionList = selectedAdditionList.toMutableList()
                    .apply {
                        remove(element = addition)
                    }
            )
        }
    }

    private fun loadData(
        selectedGroupAdditionUuid: String?,
        selectedGroupAdditionName: String,
        menuProductUuid: String
    ) {
        viewModelScope.launchSafe(
            block = {
                val additionPack = getSelectedAdditionListUseCase(
                    menuProductUuid = menuProductUuid,
                    selectedGroupAdditionUuid = selectedGroupAdditionUuid
                )
                setState {
                    copy(
                        state = SelectAdditionList.DataState.State.SUCCESS,
                        selectedAdditionList = additionPack.selectedAdditionList.map { addition ->
                            SelectAdditionList.DataState.AdditionItem(
                                uuid = addition.uuid,
                                name = addition.name
                            )
                        },
                        notSelectedAdditionList = additionPack.notSelectedAdditionList.map { addition ->
                            SelectAdditionList.DataState.AdditionItem(
                                uuid = addition.uuid,
                                name = addition.name
                            )
                        },
                        groupName = selectedGroupAdditionName
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = SelectAdditionList.DataState.State.ERROR
                    )
                }
            }
        )
    }

    private fun backClick() {
        sendEvent {
            SelectAdditionList.Event.Back
        }
    }

    private fun selectAdditionListClick(additionUuidList: List<String>) {
        sendEvent {
            SelectAdditionList.Event.SelectAdditionListBack(
                additionUuidList = additionUuidList
            )
        }
    }
}
