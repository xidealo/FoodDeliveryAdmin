package com.bunbeauty.fooddeliveryadmin.screen.statistic.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.presentation.feature.statistic.Statistic
import com.bunbeauty.presentation.feature.statistic.TimeIntervalCode
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun Statistic.DataState.toViewState(): StatisticViewState {
    return StatisticViewState(
        state = when (state) {
            Statistic.DataState.State.LOADING -> StatisticViewState.State.Loading
            Statistic.DataState.State.ERROR -> StatisticViewState.State.Error
            Statistic.DataState.State.SUCCESS -> {
                StatisticViewState.State.Success(
                    statisticList = statisticList.map { statistic ->
                        statistic.toStatisticItemModelView()
                    }.toPersistentList(),
                    period = getTimeIntervalName(
                        timeInterval = selectedTimeInterval
                    ).timeInterval,
                    loadingStatistic = loadingStatistic,
                    timeIntervalListUI = StatisticViewState.TimeIntervalListUI(
                        isShown = isTimeIntervalListShown,
                        timeIntervalList = TimeIntervalCode.entries.map { timeIntervalCode ->
                            getTimeIntervalName(
                                timeInterval = timeIntervalCode
                            )
                        }.toPersistentList()
                    ),
                    cafeAddress = cafeAddress.orEmpty()
                )
            }
        }
    )
}

@Composable
fun Statistic.DataState.StatisticItemModel.toStatisticItemModelView(): StatisticViewState.State.Success.StatisticItemModel {
    return StatisticViewState.State.Success.StatisticItemModel(
        startMillis = startMillis,
        period = period,
        count = stringResource(
            R.string.msg_statistic_orders,
            count
        ),
        proceeds = proceeds,
        date = date
    )
}

@Composable
internal fun getTimeIntervalName(timeInterval: TimeIntervalCode): StatisticViewState.TimeIntervalListUI.TimeIntervalItem {
    return when (timeInterval) {
        TimeIntervalCode.DAY -> StatisticViewState.TimeIntervalListUI.TimeIntervalItem(
            stringResource(R.string.msg_statistic_day_interval),
            timeIntervalType = TimeIntervalCode.DAY
        )

        TimeIntervalCode.WEEK ->
            StatisticViewState.TimeIntervalListUI.TimeIntervalItem(
                stringResource(R.string.msg_statistic_week_interval),
                timeIntervalType = TimeIntervalCode.WEEK
            )

        TimeIntervalCode.MONTH -> StatisticViewState.TimeIntervalListUI.TimeIntervalItem(
            stringResource(R.string.msg_statistic_month_interval),
            timeIntervalType = TimeIntervalCode.MONTH
        )
    }
}
