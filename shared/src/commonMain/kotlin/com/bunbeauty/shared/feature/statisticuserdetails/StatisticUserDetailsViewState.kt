package com.bunbeauty.shared.feature.statisticuserdetails

import DateTimeUtil
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.domain.util.datetime.PATTERN_ISO_DATE
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.common_with_ruble
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_empty_date
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt

@Immutable
data class StatisticUserDetailsViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val phoneNumber: String,
            val firstOrderDate: String,
            val lastOrderDate: String,
            val deliveryOrderCount: String,
            val pickupOrderCount: String,
            val averageCheck: String,
            val orderCount: String,
        ) : State
    }
}

@Composable
internal fun StatisticUserDetails.DataState.toViewState(): StatisticUserDetailsViewState =
    StatisticUserDetailsViewState(
        state =
            when (state) {
                StatisticUserDetails.DataState.State.LOADING -> StatisticUserDetailsViewState.State.Loading
                StatisticUserDetails.DataState.State.ERROR -> StatisticUserDetailsViewState.State.Error
                StatisticUserDetails.DataState.State.SUCCESS -> {
                    statistic?.let { statistic ->
                        StatisticUserDetailsViewState.State.Success(
                            phoneNumber = statistic.phoneNumber,
                            firstOrderDate = statistic.firstOrderDate.toDateText(),
                            lastOrderDate = statistic.lastOrderDate.toDateText(),
                            deliveryOrderCount = statistic.deliveryOrderCount.toString(),
                            pickupOrderCount = statistic.pickupOrderCount.toString(),
                            averageCheck =
                                stringResource(
                                    Res.string.common_with_ruble,
                                    statistic.averageCheck.roundToInt().toString(),
                                ),
                            orderCount = statistic.orderCount.toString(),
                        )
                    } ?: StatisticUserDetailsViewState.State.Error
                }
            },
    )

@Composable
private fun Long?.toDateText(): String =
    if (this == null) {
        stringResource(Res.string.msg_statistic_user_empty_date)
    } else {
        DateTimeUtil.formatDateTime(
            millis = this,
            pattern = PATTERN_ISO_DATE,
        )
    }
