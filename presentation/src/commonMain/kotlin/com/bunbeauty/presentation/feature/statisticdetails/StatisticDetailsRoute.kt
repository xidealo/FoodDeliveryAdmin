package com.bunbeauty.presentation.feature.statisticdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.presentation.designsystem.compose.screen.LoadingScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StatisticDetailsRouteScreen(
    viewModel: StatisticDetailsViewModel = koinViewModel(),
    goBack: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: StatisticDetails.Action ->
                viewModel.onAction(event)
            }
        }

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }

    LaunchedEffect(Unit) {
        onAction(StatisticDetails.Action.Init)
    }

    StatisticDetailsEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        goBack = goBack,
    )

    StatisticDetailsScreen(
        state = viewState,
        onAction = onAction,
        goBack = goBack,
    )
}

@Composable
private fun StatisticDetailsEffect(
    effects: List<StatisticDetails.Event>,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                StatisticDetails.Event.GoBack -> {
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun StatisticDetailsScreen(
    state: StatisticDetails.DataState,
    onAction: (StatisticDetails.Action) -> Unit,
    goBack: () -> Unit,
) {
    when (state.state) {
        StatisticDetails.DataState.State.LOADING -> LoadingScreen()
        StatisticDetails.DataState.State.SUCCESS -> Unit
        StatisticDetails.DataState.State.ERROR -> Unit
    }
}
