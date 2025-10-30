package com.bunbeauty.presentation.feature.menulist.additiongroupformenuproduct.createadditiongroupformenuproduct

import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class CreateAdditionGroupForMenuProductViewModel() :
    BaseStateViewModel<CreateAdditionGroupForMenu.DataState, CreateAdditionGroupForMenu.Action, CreateAdditionGroupForMenu.Event>(
        initState = CreateAdditionGroupForMenu.DataState(
            state = CreateAdditionGroupForMenu.DataState.State.LOADING,
            groupName = null,
            additionNameList = null,
            menuProductUuid = "",
            isLoading = false,
        )
    ) {

    override fun reduce(
        action: CreateAdditionGroupForMenu.Action,
        dataState: CreateAdditionGroupForMenu.DataState
    ) {
        when (action) {
            is CreateAdditionGroupForMenu.Action.Init -> {}
            is CreateAdditionGroupForMenu.Action.SelectAdditionGroup -> {}
            is CreateAdditionGroupForMenu.Action.SelectAdditionList -> {}
            CreateAdditionGroupForMenu.Action.OnErrorButtonClick -> {}
            CreateAdditionGroupForMenu.Action.OnAdditionGroupClick -> {}
            CreateAdditionGroupForMenu.Action.OnAdditionListClick -> {}
            CreateAdditionGroupForMenu.Action.OnSaveClick -> {}
            CreateAdditionGroupForMenu.Action.OnBackClick -> {}
        }
    }

}
