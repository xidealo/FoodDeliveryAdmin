package com.bunbeauty.shared.feature.statisticuserdiscount

import androidx.compose.runtime.Immutable
import com.bunbeauty.shared.designsystem.compose.TextFieldUi
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.error_common_something_went_wrong
import fooddeliveryadmin.shared.generated.resources.error_statistic_user_discount_percent
import fooddeliveryadmin.shared.generated.resources.error_statistic_user_discount_phone

@Immutable
data class StatisticUserDiscountViewState(
    val phoneNumber: String,
    val percentField: TextFieldUi,
    val isLoading: Boolean,
) : BaseViewState

internal fun StatisticUserDiscount.DataState.toViewState(): StatisticUserDiscountViewState =
    StatisticUserDiscountViewState(
        phoneNumber = phoneNumber.toPhoneNumberText(),
        percentField =
            TextFieldUi(
                value = percentField.value,
                isError = percentField.isError,
                errorResId =
                    when (percentError) {
                        StatisticUserDiscount.DataState.PercentError.INVALID_PERCENT ->
                            Res.string.error_statistic_user_discount_percent

                        StatisticUserDiscount.DataState.PercentError.INVALID_PHONE ->
                            Res.string.error_statistic_user_discount_phone

                        StatisticUserDiscount.DataState.PercentError.SOMETHING_WENT_WRONG,
                        StatisticUserDiscount.DataState.PercentError.NO_ERROR,
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
