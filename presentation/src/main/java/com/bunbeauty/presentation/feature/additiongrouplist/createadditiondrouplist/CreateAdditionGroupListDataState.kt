package com.bunbeauty.presentation.feature.additiongrouplist.createadditiondrouplist

import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface CreateAdditionGroupListDataState {
    data class DataState(
        val state: State,
        val isLoading: Boolean,
        val nameField: TextFieldData,
        val isShowMenuVisible: Boolean,
        val singleChoice: Boolean,
        val nameStateError: NameStateError
    ) : BaseDataState {

        enum class NameStateError {
            EMPTY_NAME,
            DUPLICATE_NAME,
            NO_ERROR
        }

        enum class State {
            SUCCESS,
            ERROR,
            LOADING
        }
    }
    sealed interface Action : BaseAction {
        data object OnBackClick : Action
        data object OnErrorStateClicked : Action
        data class CreateNameAdditionGroupListChanged(val nameGroup: String) : Action
        data object OnVisibleClick : Action
        data object OnOneAdditionVisibleClick : Action
        data object OnSaveAdditionGroupListClick : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBackEvent : Event
        data class ShowUpdateAdditionGroupSuccess(val additionGroupName: String) : Event
    }
}
