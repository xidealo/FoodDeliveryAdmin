package com.bunbeauty.shared.feature.statisticdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.shared.viewmodel.base.BaseViewState

@Immutable
data class StatisticDetailsViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Success : State

        data object Error : State
    }
}

@Composable
internal fun StatisticDetails.DataState.toViewState(): StatisticDetailsViewState =
    StatisticDetailsViewState(
        state =
            when (state) {
                StatisticDetails.DataState.State.LOADING -> StatisticDetailsViewState.State.Loading
                StatisticDetails.DataState.State.SUCCESS -> StatisticDetailsViewState.State.Success
                StatisticDetails.DataState.State.ERROR -> StatisticDetailsViewState.State.Error
            },
    )
