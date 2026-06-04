package com.bunbeauty.shared.feature.statistic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.msg_statistic_day_interval
import fooddeliveryadmin.shared.generated.resources.msg_statistic_month_interval
import fooddeliveryadmin.shared.generated.resources.msg_statistic_orders
import fooddeliveryadmin.shared.generated.resources.msg_statistic_week_interval
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.stringResource

@Immutable
data class StatisticViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val statisticList: ImmutableList<StatisticItemModel>,
            val period: String,
            val selectedIntervalCode: TimeIntervalCode,
            val timeIntervalListUI: TimeIntervalListUI,
            val cafeAddress: String,
            val loadingStatistic: Boolean,
        ) : State {
            @Immutable
            data class StatisticItemModel(
                val uuid: String,
                val startMillis: Long,
                val period: String,
                val count: String,
                val proceeds: String,
                val date: String,
            )
        }
    }

    @Immutable
    data class TimeIntervalListUI(
        val isShown: Boolean,
        val timeIntervalList: ImmutableList<TimeIntervalItem>,
    ) {
        @Immutable
        data class TimeIntervalItem(
            val timeInterval: String,
            val timeIntervalType: TimeIntervalCode,
        )
    }
}

@Composable
internal fun Statistic.DataState.toViewState(): StatisticViewState =
    StatisticViewState(
        state =
            when (state) {
                Statistic.DataState.State.LOADING -> StatisticViewState.State.Loading
                Statistic.DataState.State.ERROR -> StatisticViewState.State.Error
                Statistic.DataState.State.SUCCESS -> {
                    StatisticViewState.State.Success(
                        statisticList =
                            statisticList
                                .map { statistic ->
                                    statistic.toStatisticItemModelView()
                                }.toPersistentList(),
                        period =
                            getTimeIntervalName(
                                timeInterval = selectedTimeInterval,
                            ).timeInterval,
                        selectedIntervalCode = selectedTimeInterval,
                        loadingStatistic = loadingStatistic,
                        timeIntervalListUI =
                            StatisticViewState.TimeIntervalListUI(
                                isShown = isTimeIntervalListShown,
                                timeIntervalList =
                                    TimeIntervalCode.entries
                                        .map { timeIntervalCode ->
                                            getTimeIntervalName(
                                                timeInterval = timeIntervalCode,
                                            )
                                        }.toPersistentList(),
                            ),
                        cafeAddress = cafeAddress.orEmpty(),
                    )
                }
            },
    )

@Composable
fun Statistic.DataState.StatisticItemModel.toStatisticItemModelView(): StatisticViewState.State.Success.StatisticItemModel =
    StatisticViewState.State.Success.StatisticItemModel(
        uuid = uuid,
        startMillis = startMillis,
        period = period,
        count =
            stringResource(
                Res.string.msg_statistic_orders,
                count,
            ),
        proceeds = proceeds,
        date = date,
    )

@Composable
internal fun getTimeIntervalName(timeInterval: TimeIntervalCode): StatisticViewState.TimeIntervalListUI.TimeIntervalItem =
    when (timeInterval) {
        TimeIntervalCode.DAY ->
            StatisticViewState.TimeIntervalListUI.TimeIntervalItem(
                stringResource(Res.string.msg_statistic_day_interval),
                timeIntervalType = TimeIntervalCode.DAY,
            )

        TimeIntervalCode.WEEK ->
            StatisticViewState.TimeIntervalListUI.TimeIntervalItem(
                stringResource(Res.string.msg_statistic_week_interval),
                timeIntervalType = TimeIntervalCode.WEEK,
            )

        TimeIntervalCode.MONTH ->
            StatisticViewState.TimeIntervalListUI.TimeIntervalItem(
                stringResource(Res.string.msg_statistic_month_interval),
                timeIntervalType = TimeIntervalCode.MONTH,
            )
    }
