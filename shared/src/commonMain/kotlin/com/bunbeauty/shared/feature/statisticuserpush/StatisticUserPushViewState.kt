package com.bunbeauty.shared.feature.statisticuserpush

import androidx.compose.runtime.Immutable
import com.bunbeauty.shared.designsystem.compose.TextFieldUi
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.error_common_something_went_wrong
import fooddeliveryadmin.shared.generated.resources.error_statistic_user_push_body
import fooddeliveryadmin.shared.generated.resources.error_statistic_user_push_phone
import fooddeliveryadmin.shared.generated.resources.error_statistic_user_push_title

@Immutable
data class StatisticUserPushViewState(
    val phoneNumber: String,
    val titleField: TextFieldUi,
    val bodyField: TextFieldUi,
    val isLoading: Boolean,
) : BaseViewState

internal fun StatisticUserPush.DataState.toViewState(): StatisticUserPushViewState =
    StatisticUserPushViewState(
        phoneNumber = phoneNumber.toPhoneNumberText(),
        titleField =
            TextFieldUi(
                value = titleField.value,
                isError = titleField.isError,
                errorResId =
                    when (pushError) {
                        StatisticUserPush.DataState.PushError.INVALID_TITLE ->
                            Res.string.error_statistic_user_push_title

                        StatisticUserPush.DataState.PushError.INVALID_PHONE,
                        StatisticUserPush.DataState.PushError.SOMETHING_WENT_WRONG,
                        StatisticUserPush.DataState.PushError.INVALID_BODY,
                        StatisticUserPush.DataState.PushError.NO_ERROR,
                        ->
                            Res.string.error_common_something_went_wrong
                    },
            ),
        bodyField =
            TextFieldUi(
                value = bodyField.value,
                isError = bodyField.isError,
                errorResId =
                    when (pushError) {
                        StatisticUserPush.DataState.PushError.INVALID_BODY ->
                            Res.string.error_statistic_user_push_body

                        StatisticUserPush.DataState.PushError.INVALID_PHONE ->
                            Res.string.error_statistic_user_push_phone

                        StatisticUserPush.DataState.PushError.SOMETHING_WENT_WRONG,
                        StatisticUserPush.DataState.PushError.INVALID_TITLE,
                        StatisticUserPush.DataState.PushError.NO_ERROR,
                        ->
                            Res.string.error_common_something_went_wrong
                    },
            ),
        isLoading = isLoading,
    )

/**
 * Бэкенд отдаёт телефон в формате +7XXXXXXXXXX. Приводим к виду +7 (XXX)-XXX-XX-XX,
 * оставляя строку как есть, если формат отличается от ожидаемого.
 */
private fun String.toPhoneNumberText(): String {
    if (length != PHONE_NUMBER_LENGTH || !startsWith(PHONE_NUMBER_PREFIX)) {
        return this
    }
    val digits = substring(PHONE_NUMBER_PREFIX.length)
    return "$PHONE_NUMBER_PREFIX (${digits.substring(0, 3)})-" +
        "${digits.substring(3, 6)}-${digits.substring(6, 8)}-${digits.substring(8, 10)}"
}

private const val PHONE_NUMBER_PREFIX = "+7"
private const val PHONE_NUMBER_LENGTH = 12
