package com.bunbeauty.shared.feature.statisticuserpush

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.clientuser.SendClientPushUseCase
import com.bunbeauty.domain.feature.clientuser.exception.ClientPhoneNumberException
import com.bunbeauty.domain.feature.clientuser.exception.ClientPushBodyException
import com.bunbeauty.domain.feature.clientuser.exception.ClientPushTitleException
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.feature.menulist.common.TextFieldData
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel

class StatisticUserPushViewModel(
    private val sendClientPushUseCase: SendClientPushUseCase,
) : BaseStateViewModel<StatisticUserPush.DataState, StatisticUserPush.Action, StatisticUserPush.Event>(
        initState =
            StatisticUserPush.DataState(
                phoneNumber = "",
                titleField = TextFieldData.empty,
                bodyField = TextFieldData.empty,
                pushError = StatisticUserPush.DataState.PushError.NO_ERROR,
                isLoading = false,
            ),
    ) {
    override fun reduce(
        action: StatisticUserPush.Action,
        dataState: StatisticUserPush.DataState,
    ) {
        when (action) {
            is StatisticUserPush.Action.Init -> handleInit(phoneNumber = action.phoneNumber)

            is StatisticUserPush.Action.TitleChanged -> handleTitleChanged(action.title)

            is StatisticUserPush.Action.BodyChanged -> handleBodyChanged(action.body)

            StatisticUserPush.Action.OnSendClick -> handleSendClick(dataState)

            StatisticUserPush.Action.BackClick ->
                sendEvent {
                    StatisticUserPush.Event.GoBack
                }
        }
    }

    private fun handleInit(phoneNumber: String) {
        if (state.value.phoneNumber == phoneNumber) {
            return
        }
        setState {
            copy(
                phoneNumber = phoneNumber,
                titleField = TextFieldData.empty,
                bodyField = TextFieldData.empty,
                pushError = StatisticUserPush.DataState.PushError.NO_ERROR,
            )
        }
    }

    private fun handleTitleChanged(title: String) {
        setState {
            copy(
                titleField =
                    titleField.copy(
                        value = title,
                        isError = false,
                    ),
                pushError = StatisticUserPush.DataState.PushError.NO_ERROR,
            )
        }
    }

    private fun handleBodyChanged(body: String) {
        setState {
            copy(
                bodyField =
                    bodyField.copy(
                        value = body,
                        isError = false,
                    ),
                pushError = StatisticUserPush.DataState.PushError.NO_ERROR,
            )
        }
    }

    private fun handleSendClick(dataState: StatisticUserPush.DataState) {
        if (dataState.isLoading) {
            return
        }

        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        isLoading = true,
                        titleField = titleField.copy(isError = false),
                        bodyField = bodyField.copy(isError = false),
                        pushError = StatisticUserPush.DataState.PushError.NO_ERROR,
                    )
                }
                sendClientPushUseCase(
                    phoneNumber = dataState.phoneNumber,
                    title = dataState.titleField.value,
                    body = dataState.bodyField.value,
                )
                setState {
                    copy(isLoading = false)
                }
                sendEvent {
                    StatisticUserPush.Event.ShowSavedMessage
                }
            },
            onError = { throwable ->
                setPushError(
                    when (throwable) {
                        is ClientPushTitleException -> StatisticUserPush.DataState.PushError.INVALID_TITLE
                        is ClientPushBodyException -> StatisticUserPush.DataState.PushError.INVALID_BODY
                        is ClientPhoneNumberException -> StatisticUserPush.DataState.PushError.INVALID_PHONE
                        else -> StatisticUserPush.DataState.PushError.SOMETHING_WENT_WRONG
                    },
                )
            },
        )
    }

    private fun setPushError(pushError: StatisticUserPush.DataState.PushError) {
        setState {
            copy(
                isLoading = false,
                titleField =
                    titleField.copy(
                        isError = pushError == StatisticUserPush.DataState.PushError.INVALID_TITLE,
                    ),
                bodyField =
                    bodyField.copy(
                        isError =
                            pushError == StatisticUserPush.DataState.PushError.INVALID_BODY ||
                                pushError == StatisticUserPush.DataState.PushError.INVALID_PHONE ||
                                pushError == StatisticUserPush.DataState.PushError.SOMETHING_WENT_WRONG,
                    ),
                pushError = pushError,
            )
        }
    }
}
