package com.bunbeauty.shared.feature.statisticuserpush

import com.bunbeauty.shared.feature.menulist.common.TextFieldData
import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent

interface StatisticUserPush {
    data class DataState(
        val phoneNumber: String,
        val titleField: TextFieldData,
        val bodyField: TextFieldData,
        val pushError: PushError,
        val isLoading: Boolean,
    ) : BaseDataState {
        enum class PushError {
            INVALID_TITLE,
            INVALID_BODY,
            INVALID_PHONE,
            SOMETHING_WENT_WRONG,
            NO_ERROR,
        }
    }

    sealed interface Action : BaseAction {
        data class Init(
            val phoneNumber: String,
        ) : Action

        data class TitleChanged(
            val title: String,
        ) : Action

        data class BodyChanged(
            val body: String,
        ) : Action

        data object OnSendClick : Action

        data object BackClick : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBack : Event

        data object ShowSavedMessage : Event
    }
}
