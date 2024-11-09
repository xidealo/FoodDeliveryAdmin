package com.bunbeauty.presentation.feature.additionlist.createaddition

import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface CreateAddition {
    data class DataState(
        val name: TextFieldData,
        val hasCreateNameError: Boolean,
        val priority: TextFieldData,
        val price: TextFieldData,
        val hasCreatePriceError: Boolean,
        val fullName: String,
        val isLoading: Boolean,
        val isVisible: Boolean,
        val photoLink: String
    ) : BaseDataState

    sealed interface Action : BaseAction {

        data object CreateAddition : Action
        data class CreateNameAddition(val name: String) : Action
        data class CreatePriorityAddition(val priority: String) : Action
        data class CreateFullNameAddition(val fullName: String) : Action
        data class CreatePriceAddition(val price: String) : Action
        data object OnSaveCreateAdditionClick : Action
        data class OnVisibleClick(val isVisible: Boolean) : Action
        data object OnBackClick : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        data object ShowSomethingWentWrong : Event
        data class ShowSaveAdditionSuccess(val additionName: String) : Event
    }
}