package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.GetAdditionGroupListFromMenuProductUseCase
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class AdditionGroupForMenuProductViewModel(
    private val getAdditionGroupListFromMenuProductUseCase: GetAdditionGroupListFromMenuProductUseCase
) :
    BaseStateViewModel<AdditionGroupForMenuProduct.DataState, AdditionGroupForMenuProduct.Action, AdditionGroupForMenuProduct.Event>(
        initState = AdditionGroupForMenuProduct.DataState(
            additionGroupList = listOf()
        )
    ) {

    override fun reduce(
        action: AdditionGroupForMenuProduct.Action,
        dataState: AdditionGroupForMenuProduct.DataState
    ) {
        when (action) {
            is AdditionGroupForMenuProduct.Action.Init -> loadData(
                menuProductUuid = action.menuProductUuid
            )

            is AdditionGroupForMenuProduct.Action.OnAdditionGroupClick -> onAdditionGroupClick(
                uuid = action.uuid
            )

            AdditionGroupForMenuProduct.Action.OnBackClick -> backClick()
        }
    }

    fun loadData(menuProductUuid: String) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        additionGroupList = getAdditionGroupListFromMenuProductUseCase(
                            menuProductUuid = menuProductUuid
                        )
                    )
                }
            },
            onError = {
                // handle error
            }
        )
    }

    fun onAdditionGroupClick(uuid: String) {
        sendEvent {
            AdditionGroupForMenuProduct.Event.OnAdditionGroupClick(uuid = uuid)
        }
    }

    fun backClick() {
        sendEvent {
            AdditionGroupForMenuProduct.Event.Back
        }
    }
}
