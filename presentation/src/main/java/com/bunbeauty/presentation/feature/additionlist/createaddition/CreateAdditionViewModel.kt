package com.bunbeauty.presentation.feature.additionlist.createaddition

import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class CreateAdditionViewModel() :
    BaseStateViewModel<CreateAddition.DataState, CreateAddition.Action, CreateAddition.Event>(
        initState = CreateAddition.DataState(
            uuid = "",
            name = "",
            priority = "",
            price = "",
            isLoading = true,
            isVisible = false,
            fullName = "",
            hasEditNameError = false,
            hasEditPriorityError = false,
            tag = "",
            //  imageFieldData = EditImageFieldData(
            //      value = null,
            //      isError = false
            //  )
        )
    ) {
    override fun reduce(
        action: CreateAddition.Action,
        dataState: CreateAddition.DataState
    ) {
        when (action) {
            is CreateAddition.Action.EditFullNameAddition -> TODO()
            is CreateAddition.Action.EditNameAddition -> TODO()
            is CreateAddition.Action.EditPriceAddition -> TODO()
            is CreateAddition.Action.EditPriorityAddition -> TODO()
            is CreateAddition.Action.EditTagAddition -> TODO()
            CreateAddition.Action.OnBackClick -> TODO()
            CreateAddition.Action.OnSaveCreateAdditionClick -> TODO()
            is CreateAddition.Action.OnVisibleClick -> TODO()
            is CreateAddition.Action.SetImage -> TODO()
        }
    }
}