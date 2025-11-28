package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.additionlist.GetAdditionListNameUseCase
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.GetAdditionGroupListFromMenuProductUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class AdditionGroupForMenuProductListViewModel(
    private val getAdditionGroupListFromMenuProductUseCase: GetAdditionGroupListFromMenuProductUseCase,
    private val getAdditionListNameUseCase: GetAdditionListNameUseCase
) :
    BaseStateViewModel<AdditionGroupForMenuProductList.DataState, AdditionGroupForMenuProductList.Action, AdditionGroupForMenuProductList.Event>(
        initState = AdditionGroupForMenuProductList.DataState(
            additionGroupList = listOf(),
            state = AdditionGroupForMenuProductList.DataState.State.LOADING
        )
    ) {

    override fun reduce(
        action: AdditionGroupForMenuProductList.Action,
        dataState: AdditionGroupForMenuProductList.DataState
    ) {
        when (action) {
            is AdditionGroupForMenuProductList.Action.Init -> loadData(
                menuProductUuid = action.menuProductUuid
            )

            is AdditionGroupForMenuProductList.Action.OnAdditionGroupClick -> onAdditionGroupClick(
                additionGroupUuid = action.uuid
            )

            AdditionGroupForMenuProductList.Action.OnBackClick -> backClick()
        }
    }

    fun loadData(menuProductUuid: String) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        state = AdditionGroupForMenuProductList.DataState.State.LOADING
                    )
                }

                val additionGroupList = getAdditionGroupListFromMenuProductUseCase(
                    menuProductUuid = menuProductUuid
                ).map { additionGroupList ->
                    AdditionGroupForMenuProductList.DataState.AdditionGroupForMenuProduct(
                        uuid = additionGroupList.additionGroup.uuid,
                        name = additionGroupList.additionGroup.name,
                        additionNameList = getAdditionListNameUseCase(
                            additionList = additionGroupList.additionList
                        )
                    )
                }

                setState {
                    copy(
                        additionGroupList = additionGroupList,
                        state = AdditionGroupForMenuProductList.DataState.State.SUCCESS
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = AdditionGroupForMenuProductList.DataState.State.ERROR
                    )
                }
            }
        )
    }

    fun onAdditionGroupClick(additionGroupUuid: String) {
        sendEvent {
            AdditionGroupForMenuProductList.Event.OnAdditionGroupClick(
                additionGroupUuid = additionGroupUuid
            )
        }
    }

    fun backClick() {
        sendEvent {
            AdditionGroupForMenuProductList.Event.Back
        }
    }
}
