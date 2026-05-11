package com.bunbeauty.shared.feature.statisticdetails

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.usecase.GetStatisticDayDetailUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.feature.statistic.TimeIntervalCode
import com.bunbeauty.shared.feature.statistic.toStatisticDetailPeriod
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel

class StatisticDetailsViewModel(
    private val getStatisticDayDetailUseCase: GetStatisticDayDetailUseCase,
) : BaseStateViewModel<StatisticDetails.DataState, StatisticDetails.Action, StatisticDetails.Event>(
        initState =
            StatisticDetails.DataState(
                state = StatisticDetails.DataState.State.LOADING,
            ),
    ) {
    override fun reduce(
        action: StatisticDetails.Action,
        dataState: StatisticDetails.DataState,
    ) {
        when (action) {
            is StatisticDetails.Action.Init -> {
                loadDayDetail(
                    dateIso = action.dateIso,
                    period = action.period,
                )
            }

            StatisticDetails.Action.Retry -> {
                if (dataState.loadedDateIso.isNotEmpty()) {
                    loadDayDetail(
                        dateIso = dataState.loadedDateIso,
                        period = dataState.loadedPeriod,
                    )
                }
            }

            StatisticDetails.Action.SelectGoBackClick -> {
                sendEvent {
                    StatisticDetails.Event.GoBack
                }
            }
        }
    }

    private fun loadDayDetail(
        dateIso: String,
        period: TimeIntervalCode,
    ) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(
                        state = StatisticDetails.DataState.State.LOADING,
                        loadedDateIso = dateIso,
                        loadedPeriod = period,
                        dayDetail = null,
                    )
                }
                val detail =
                    getStatisticDayDetailUseCase(
                        dateIso,
                        period.toStatisticDetailPeriod(),
                    )
                setState {
                    copy(
                        state = StatisticDetails.DataState.State.SUCCESS,
                        dayDetail = detail,
                        loadedDateIso = dateIso,
                        loadedPeriod = period,
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        state = StatisticDetails.DataState.State.ERROR,
                        loadedDateIso = dateIso,
                        loadedPeriod = period,
                    )
                }
            },
        )
    }
}
