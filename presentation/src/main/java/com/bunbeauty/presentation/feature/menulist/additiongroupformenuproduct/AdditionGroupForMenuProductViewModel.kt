package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct

import com.bunbeauty.domain.feature.menu.additiongroupformenuproduct.GetAdditionGroupListFromMenuProductUseCase
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class AdditionGroupForMenuProductViewModel(
    private val getAdditionGroupListFromMenuProductUseCase: GetAdditionGroupListFromMenuProductUseCase
) :
    BaseStateViewModel<AdditionGroupForMenuProduct.DataState, AdditionGroupForMenuProduct.Action, AdditionGroupForMenuProduct.Event>(
        initState = AdditionGroupForMenuProduct.DataState(
            additionGroupList = listOf(),
        )
    ) {

    override fun reduce(
        action: AdditionGroupForMenuProduct.Action,
        dataState: AdditionGroupForMenuProduct.DataState
    ) {
        when (action) {
            AdditionGroupForMenuProduct.Action.Init -> loadData()
            is AdditionGroupForMenuProduct.Action.OnAdditionGroupClick -> onAdditionGroupClick(
                uuid = action.uuid
            )

            AdditionGroupForMenuProduct.Action.OnBackClick -> backClick()
        }
    }

    fun loadData() {

    }

    fun onAdditionGroupClick(uuid: String) {

    }

    fun backClick() {
        sendEvent {
            AdditionGroupForMenuProduct.Event.Back
        }
    }

}