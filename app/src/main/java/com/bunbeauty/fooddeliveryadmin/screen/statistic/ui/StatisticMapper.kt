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
        statisticList = statisticList.map { statistic ->
            statistic.toStatisticItemModelView()
        }.toPersistentList(),
        selectedCafe = selectedCafe?.address
            ?: stringResource(R.string.msg_statistic_all_cafes),
        period = getTimeIntervalName(
            timeInterval = selectedTimeInterval
        ).timeInterval,
        isLoading = isLoading,
        hasError = hasError,
        timeIntervalListUI = TimeIntervalListUI(
            isShown = isTimeIntervalListShown,
            timeIntervalList = TimeIntervalCode.entries.map { timeIntervalCode ->
                getTimeIntervalName(
                    timeInterval = timeIntervalCode
                )
            }.toPersistentList()
        ),
        cafeListUI = CafeListUI(
            isShown = isCafeListShown,
            cafeList = buildList {
                add(
                    CafeListUI.CafeItem(
                        uuid = null,
                        name = stringResource(R.string.msg_statistic_all_cafes)
                    )
                )
                cafeList.map { cafe ->
                    CafeListUI.CafeItem(
                        uuid = cafe.uuid,
                        name = cafe.address
                    )
                }.let { cafeAddressList ->
                    addAll(cafeAddressList)
                }
            }.toPersistentList()

        )
    )
}

@Composable
fun Statistic.DataState.StatisticItemModel.toStatisticItemModelView(): StatisticViewState.StatisticItemModel {
    return StatisticViewState.StatisticItemModel(
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
internal fun getTimeIntervalName(timeInterval: TimeIntervalCode): TimeIntervalListUI.TimeIntervalItem {
    return when (timeInterval) {
        TimeIntervalCode.DAY -> TimeIntervalListUI.TimeIntervalItem(
            stringResource(R.string.msg_statistic_day_interval),
            timeIntervalType = TimeIntervalCode.DAY
        )

        TimeIntervalCode.WEEK ->
            TimeIntervalListUI.TimeIntervalItem(
                stringResource(R.string.msg_statistic_week_interval),
                timeIntervalType = TimeIntervalCode.WEEK
            )

        TimeIntervalCode.MONTH -> TimeIntervalListUI.TimeIntervalItem(
            stringResource(R.string.msg_statistic_month_interval),
            timeIntervalType = TimeIntervalCode.MONTH
        )
    }
}
