package com.bunbeauty.shared.feature.statisticuserpush

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.clientuser.SendClientPushUseCase
import com.bunbeauty.domain.feature.clientuser.exception.ClientPhoneNumberException
import com.bunbeauty.domain.feature.clientuser.exception.ClientPushBodyException
import com.bunbeauty.domain.feature.clientuser.exception.ClientPushTitleException
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.feature.menulist.common.TextFieldData
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.error_common_something_went_wrong
import fooddeliveryadmin.shared.generated.resources.error_statistic_user_push_phone

class StatisticUserPushViewModel(
    private val sendClientPushUseCase: SendClientPushUseCase,
) : BaseStateViewModel<StatisticUserPush.DataState, StatisticUserPush.Action, StatisticUserPush.Event>(
        initState =
            StatisticUserPush.DataState(
                phoneNumber = "",
                customTitleField = TextFieldData.empty,
                customBodyField = TextFieldData.empty,
                sendingPush = null,
                pushError = StatisticUserPush.DataState.PushError.NO_ERROR,
            ),
    ) {
    override fun reduce(
        action: StatisticUserPush.Action,
        dataState: StatisticUserPush.DataState,
    ) {
        when (action) {
            is StatisticUserPush.Action.Init -> handleInit(phoneNumber = action.phoneNumber)

            is StatisticUserPush.Action.QuickPushClick ->
                handleQuickPushClick(
                    dataState = dataState,
                    template = action.template,
                )

            is StatisticUserPush.Action.CustomTitleChanged -> handleCustomTitleChanged(action.title)

            is StatisticUserPush.Action.CustomBodyChanged -> handleCustomBodyChanged(action.body)

            StatisticUserPush.Action.SendCustomClick -> handleSendCustomClick(dataState)

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
                customTitleField = TextFieldData.empty,
                customBodyField = TextFieldData.empty,
                sendingPush = null,
                pushError = StatisticUserPush.DataState.PushError.NO_ERROR,
            )
        }
    }

    private fun handleCustomTitleChanged(title: String) {
        setState {
            copy(
                customTitleField =
                    customTitleField.copy(
                        value = title,
                        isError = false,
                    ),
                pushError = StatisticUserPush.DataState.PushError.NO_ERROR,
            )
        }
    }

    private fun handleCustomBodyChanged(body: String) {
        setState {
            copy(
                customBodyField =
                    customBodyField.copy(
                        value = body,
                        isError = false,
                    ),
                pushError = StatisticUserPush.DataState.PushError.NO_ERROR,
            )
        }
    }

    private fun handleQuickPushClick(
        dataState: StatisticUserPush.DataState,
        template: QuickPushTemplate,
    ) {
        if (dataState.sendingPush != null) {
            return
        }

        val sendingPush = template.toSendingPush()
        viewModelScope.launchSafe(
            block = {
                sendPush(
                    dataState = dataState,
                    sendingPush = sendingPush,
                    title = template.titleVariants.random(),
                    body = template.bodyVariants.random(),
                )
            },
            onError = { throwable ->
                handleSendError(
                    sendingPush = sendingPush,
                    throwable = throwable,
                )
            },
        )
    }

    private fun handleSendCustomClick(dataState: StatisticUserPush.DataState) {
        if (dataState.sendingPush != null) {
            return
        }

        val sendingPush = StatisticUserPush.DataState.SendingPush.CUSTOM
        viewModelScope.launchSafe(
            block = {
                sendPush(
                    dataState = dataState,
                    sendingPush = sendingPush,
                    title = dataState.customTitleField.value,
                    body = dataState.customBodyField.value,
                )
            },
            onError = { throwable ->
                handleSendError(
                    sendingPush = sendingPush,
                    throwable = throwable,
                )
            },
        )
    }

    private suspend fun sendPush(
        dataState: StatisticUserPush.DataState,
        sendingPush: StatisticUserPush.DataState.SendingPush,
        title: String,
        body: String,
    ) {
        setState {
            copy(
                sendingPush = sendingPush,
                customTitleField = customTitleField.copy(isError = false),
                customBodyField = customBodyField.copy(isError = false),
                pushError = StatisticUserPush.DataState.PushError.NO_ERROR,
            )
        }
        sendClientPushUseCase(
            phoneNumber = dataState.phoneNumber,
            title = title,
            body = body,
        )
        setState {
            copy(sendingPush = null)
        }
        sendEvent {
            StatisticUserPush.Event.ShowSentMessage
        }
    }

    private fun handleSendError(
        sendingPush: StatisticUserPush.DataState.SendingPush,
        throwable: Throwable,
    ) {
        val pushError =
            when (throwable) {
                is ClientPushTitleException -> StatisticUserPush.DataState.PushError.INVALID_TITLE
                is ClientPushBodyException -> StatisticUserPush.DataState.PushError.INVALID_BODY
                is ClientPhoneNumberException -> StatisticUserPush.DataState.PushError.INVALID_PHONE
                else -> StatisticUserPush.DataState.PushError.SOMETHING_WENT_WRONG
            }
        val isCustomTitleError =
            pushError == StatisticUserPush.DataState.PushError.INVALID_TITLE &&
                sendingPush == StatisticUserPush.DataState.SendingPush.CUSTOM
        val isCustomBodyError =
            pushError == StatisticUserPush.DataState.PushError.INVALID_BODY &&
                sendingPush == StatisticUserPush.DataState.SendingPush.CUSTOM

        setState {
            copy(
                sendingPush = null,
                customTitleField = customTitleField.copy(isError = isCustomTitleError),
                customBodyField = customBodyField.copy(isError = isCustomBodyError),
                pushError = pushError,
            )
        }

        if (!isCustomTitleError && !isCustomBodyError) {
            sendEvent {
                StatisticUserPush.Event.ShowErrorMessage(
                    messageResource =
                        when (pushError) {
                            StatisticUserPush.DataState.PushError.INVALID_PHONE ->
                                Res.string.error_statistic_user_push_phone

                            else -> Res.string.error_common_something_went_wrong
                        },
                )
            }
        }
    }
}

private fun QuickPushTemplate.toSendingPush(): StatisticUserPush.DataState.SendingPush =
    when (this) {
        QuickPushTemplate.RARE_ORDERS -> StatisticUserPush.DataState.SendingPush.RARE_ORDERS
        QuickPushTemplate.NEW_MENU -> StatisticUserPush.DataState.SendingPush.NEW_MENU
    }
