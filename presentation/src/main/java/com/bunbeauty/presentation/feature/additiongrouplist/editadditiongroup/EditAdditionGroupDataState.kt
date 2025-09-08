package com.bunbeauty.presentation.feature.additiongrouplist.editadditiongroup

import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent

interface EditAdditionGroupDataState {
    data class DataState(
        val uuid: String,
        val name: TextFieldData,
        val state: State,
        val isLoading: Boolean,
        val isVisible: Boolean,
        val isVisibleSingleChoice: Boolean,
        val nameStateError: NameStateError
    ) : BaseDataState {

        enum class NameStateError {
            EMPTY_NAME,
            DUPLICATE_NAME,
            NO_ERROR
        }

        enum class State {
            LOADING,
            SUCCESS,
            ERROR
        }
    }

    sealed interface Action : BaseAction {
        data class EditNameAdditionGroup(val nameEditAdditionGroup: String) : Action
        data object OnBackClicked : Action
        data object Init : Action
        data object OnSaveEditAdditionGroupClick : Action
        data class OnVisibleMenu(val isVisible: Boolean) : Action
        data class OnVisibleSingleChoice(val isVisibleSingleChoice: Boolean) : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBackEvent : Event
        data class ShowUpdateAdditionGroupSuccess(val additionGroupName: String) : Event
    }
}
