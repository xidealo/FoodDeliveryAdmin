package com.bunbeauty.presentation.feature.statisticdetails

import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class StatisticDetailsViewModel :
    BaseStateViewModel<StatisticDetails.DataState, StatisticDetails.Action, StatisticDetails.Event>(
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
            StatisticDetails.Action.Init -> {
            }
        }
    }
}
