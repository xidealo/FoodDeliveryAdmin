package com.bunbeauty.shared.feature.statisticdetails

import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel

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
