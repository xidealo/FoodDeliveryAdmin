package com.bunbeauty.shared.feature.statistic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.LocalBottomBarPadding
import com.bunbeauty.shared.designsystem.compose.bottomBarPadding
import com.bunbeauty.shared.designsystem.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.shared.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCard
import com.bunbeauty.shared.designsystem.compose.element.card.NavigationTextCard
import com.bunbeauty.shared.designsystem.compose.element.card.TextWithHintCard
import com.bunbeauty.shared.designsystem.compose.element.selectable.SelectableItem
import com.bunbeauty.shared.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.bold
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_product_statistic_load
import fooddeliveryadmin.shared.generated.resources.msg_common_cafe
import fooddeliveryadmin.shared.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.shared.generated.resources.msg_common_period
import fooddeliveryadmin.shared.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.shared.generated.resources.title_statistic
import fooddeliveryadmin.shared.generated.resources.title_statistic_select_time_interval
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StatisticRouteScreen(
    viewModel: StatisticViewModel = koinViewModel(),
    goBack: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: Statistic.Action ->
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
        onAction(Statistic.Action.Init)
    }

    StatisticEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        goBack = goBack,
    )

    StatisticScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        goBack = goBack,
    )
}

@Composable
private fun StatisticEffect(
    effects: List<Statistic.Event>,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                Statistic.Event.GoBack -> {
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun StatisticScreen(
    state: StatisticViewState,
    onAction: (Statistic.Action) -> Unit,
    goBack: () -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_statistic),
        backActionClick = { onAction(Statistic.Action.SelectGoBackClick) },
        actionButton = {
            if (state.state is StatisticViewState.State.Success) {
                LoadingButton(
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp)
                            .bottomBarPadding(),
                    text = stringResource(Res.string.action_product_statistic_load),
                    isLoading = state.state.loadingStatistic,
                    onClick = {
                        onAction(Statistic.Action.LoadStatisticClick)
                    },
                )
            }
        },
    ) {
        when (state.state) {
            StatisticViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(Statistic.Action.Init)
                    },
                )
            }

            StatisticViewState.State.Loading -> LoadingScreen()
            is StatisticViewState.State.Success -> {
                Column {
                    TextWithHintCard(
                        modifier =
                            Modifier
                                .padding(top = 16.dp)
                                .padding(horizontal = 16.dp),
                        hint = stringResource(Res.string.msg_common_cafe),
                        label = state.state.cafeAddress,
                    )

                    NavigationTextCard(
                        modifier =
                            Modifier
                                .padding(vertical = 8.dp)
                                .padding(horizontal = 16.dp),
                        labelText = stringResource(Res.string.msg_common_period),
                        valueText = state.state.period,
                        onClick = {
                            onAction(Statistic.Action.SelectTimeIntervalClick)
                        },
                    )

                    if (!state.state.loadingStatistic) {
                        StatisticSuccessScreen(
                            state = state.state,
                            onAction = onAction,
                        )
                    }
                }
            }
        }
    }
}

@Suppress("NonSkippableComposable")
@Composable
private fun StatisticSuccessScreen(
    state: StatisticViewState.State.Success,
    onAction: (Statistic.Action) -> Unit,
) {
    val listState = rememberLazyListState()

    LazyColumn(
        contentPadding =
            PaddingValues(
                top = 8.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace + LocalBottomBarPadding.current,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState,
    ) {
        items(
            items = state.statisticList,
            key = { statisticItemModel ->
                statisticItemModel.startMillis
            },
        ) { statisticItemModel ->
            StatisticItem(statisticItemModel)
        }
    }

    LaunchedEffect(state.statisticList) {
        listState.animateScrollToItem(0)
    }

    TimeIntervalListBottomSheet(state = state, onAction = onAction)
}

@Suppress("NonSkippableComposable")
@Composable
private fun TimeIntervalListBottomSheet(
    state: StatisticViewState.State.Success,
    onAction: (Statistic.Action) -> Unit,
) {
    AdminModalBottomSheet(
        title = stringResource(Res.string.title_statistic_select_time_interval),
        isShown = state.timeIntervalListUI.isShown,
        onDismissRequest = {
            onAction(Statistic.Action.CloseTimeIntervalListBottomSheet)
        },
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
        ) {
            state.timeIntervalListUI.timeIntervalList.forEach { timeInterval ->
                SelectableItem(
                    title = timeInterval.timeInterval,
                    clickable = true,
                    elevated = false,
                    onClick = {
                        onAction(
                            Statistic.Action.SelectedTimeInterval(
                                timeInterval = timeInterval.timeIntervalType,
                            ),
                        )
                    },
                )
            }
        }
    }
}

@Suppress("NonSkippableComposable")
@Composable
private fun StatisticItem(statisticItemModel: StatisticViewState.State.Success.StatisticItemModel) {
    AdminCard(
        modifier = Modifier.fillMaxWidth(),
        clickable = false,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp,
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier =
                    Modifier
                        .weight(1f),
                text = statisticItemModel.date,
                style = AdminTheme.typography.titleSmall,
                color = AdminTheme.colors.main.onSurface,
            )
            Column(
                modifier =
                    Modifier
                        .padding(
                            start = 8.dp,
                        ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = statisticItemModel.count,
                    style = AdminTheme.typography.bodySmall,
                    color = AdminTheme.colors.main.onSurface,
                )
                Text(
                    modifier =
                        Modifier
                            .padding(
                                top = 4.dp,
                            ),
                    text = statisticItemModel.proceeds,
                    style = AdminTheme.typography.bodyMedium.bold,
                    color = AdminTheme.colors.main.onSurface,
                )
            }
        }
    }
}

@Suppress("NonSkippableComposable")
@Preview()
@Composable
private fun StatisticScreenPreview() {
    AdminTheme {
        StatisticScreen(
            state =
                StatisticViewState(
                    state =
                        StatisticViewState.State.Success(
                            statisticList =
                                persistentListOf(
                                    StatisticViewState.State.Success.StatisticItemModel(
                                        startMillis = 3064,
                                        period = "апрель",
                                        count = "Заказов: 20",
                                        proceeds = "2000 $",
                                        date = "ssss",
                                    ),
                                    StatisticViewState.State.Success.StatisticItemModel(
                                        startMillis = 3064,
                                        period = "май",
                                        count = "Заказов: 387",
                                        proceeds = "128234 $",
                                        date = "ssss",
                                    ),
                                ),
                            period = "По годам",
                            timeIntervalListUI =
                                StatisticViewState.TimeIntervalListUI(
                                    isShown = false,
                                    timeIntervalList = persistentListOf(),
                                ),
                            loadingStatistic = false,
                            cafeAddress = "Кимры чупки 22 в",
                        ),
                ),
            onAction = {},
            goBack = {},
        )
    }
}
