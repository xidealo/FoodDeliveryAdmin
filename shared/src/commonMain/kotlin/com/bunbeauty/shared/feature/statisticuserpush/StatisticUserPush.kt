package com.bunbeauty.shared.feature.statisticuserpush

import com.bunbeauty.shared.feature.menulist.common.TextFieldData
import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent
import org.jetbrains.compose.resources.StringResource

interface StatisticUserPush {
    data class DataState(
        val phoneNumber: String,
        val customTitleField: TextFieldData,
        val customBodyField: TextFieldData,
        val sendingPush: SendingPush?,
        val pushError: PushError,
    ) : BaseDataState {
        enum class SendingPush {
            RARE_ORDERS,
            NEW_MENU,
            CUSTOM,
        }

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

        data class QuickPushClick(
            val template: QuickPushTemplate,
        ) : Action

        data class CustomTitleChanged(
            val title: String,
        ) : Action

        data class CustomBodyChanged(
            val body: String,
        ) : Action

        data object SendCustomClick : Action

        data object BackClick : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBack : Event

        data object ShowSentMessage : Event

        data class ShowErrorMessage(
            val messageResource: StringResource,
        ) : Event
    }
}
