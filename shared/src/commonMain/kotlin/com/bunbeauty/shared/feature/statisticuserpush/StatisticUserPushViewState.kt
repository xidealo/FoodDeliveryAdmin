package com.bunbeauty.shared.feature.statisticuserpush

import androidx.compose.runtime.Immutable
import com.bunbeauty.shared.designsystem.compose.TextFieldUi
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.error_statistic_user_push_body
import fooddeliveryadmin.shared.generated.resources.error_statistic_user_push_title

@Immutable
data class StatisticUserPushViewState(
    val quickPushes: List<QuickPushUi>,
    val customTitleField: TextFieldUi,
    val customBodyField: TextFieldUi,
    val isCustomSending: Boolean,
    val areActionsEnabled: Boolean,
) : BaseViewState

@Immutable
data class QuickPushUi(
    val template: QuickPushTemplate,
    val isEnabled: Boolean,
    val isSending: Boolean,
)

internal fun StatisticUserPush.DataState.toViewState(): StatisticUserPushViewState {
    val areActionsEnabled = sendingPush == null

    return StatisticUserPushViewState(
        quickPushes =
            QuickPushTemplate.entries.map { template ->
                QuickPushUi(
                    template = template,
                    isEnabled = areActionsEnabled,
                    isSending =
                        when (template) {
                            QuickPushTemplate.RARE_ORDERS ->
                                sendingPush == StatisticUserPush.DataState.SendingPush.RARE_ORDERS

                            QuickPushTemplate.NEW_MENU ->
                                sendingPush == StatisticUserPush.DataState.SendingPush.NEW_MENU
                        },
                )
            },
        customTitleField =
            TextFieldUi(
                value = customTitleField.value,
                isError = customTitleField.isError,
                errorResId = Res.string.error_statistic_user_push_title,
            ),
        customBodyField =
            TextFieldUi(
                value = customBodyField.value,
                isError = customBodyField.isError,
                errorResId = Res.string.error_statistic_user_push_body,
            ),
        isCustomSending = sendingPush == StatisticUserPush.DataState.SendingPush.CUSTOM,
        areActionsEnabled = areActionsEnabled,
    )
}
