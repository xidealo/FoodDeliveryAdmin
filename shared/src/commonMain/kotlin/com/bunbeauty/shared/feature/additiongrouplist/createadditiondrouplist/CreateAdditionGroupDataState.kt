package com.bunbeauty.shared.feature.additiongrouplist.createadditiondrouplist

import com.bunbeauty.shared.feature.menulist.common.TextFieldData
import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent

interface CreateAdditionGroupDataState {
    data class DataState(
        val state: State,
        val isLoading: Boolean,
        val nameField: TextFieldData,
        val isShowMenuVisible: Boolean,
        val singleChoice: Boolean,
        val nameStateError: NameStateError,
    ) : BaseDataState {
        enum class NameStateError {
            EMPTY_NAME,
            DUPLICATE_NAME,
            NO_ERROR,
        }

        enum class State {
            SUCCESS,
            ERROR,
            LOADING,
        }
    }

    sealed interface Action : BaseAction {
        data object OnBackClick : Action

        data object OnErrorStateClicked : Action

        data class CreateNameAdditionGroupChanged(
            val nameGroup: String,
        ) : Action

        data object OnVisibleClick : Action

        data object OnOneAdditionVisibleClick : Action

        data object OnSaveAdditionGroupClick : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBackEvent : Event

        data class ShowUpdateAdditionGroupSuccess(
            val additionGroupName: String,
        ) : Event
    }
}
