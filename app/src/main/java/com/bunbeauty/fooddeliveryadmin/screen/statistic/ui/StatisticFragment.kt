package com.bunbeauty.fooddeliveryadmin.screen.statistic.ui

import android.os.Bundle
import android.view.View
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationTextCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.TextWithHintCard
import com.bunbeauty.fooddeliveryadmin.compose.element.selectable.SelectableItem
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.statistic.Statistic
import com.bunbeauty.presentation.feature.statistic.StatisticViewModel
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class StatisticFragment : BaseComposeFragment<Statistic.DataState, StatisticViewState, Statistic.Action, Statistic.Event>() {
    override val viewModel: StatisticViewModel by viewModel()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAction(
            Statistic.Action.Init,
        )
    }

    @Suppress("NonSkippableComposable")
    @Composable
    override fun Screen(
        state: StatisticViewState,
        onAction: (Statistic.Action) -> Unit,
    ) {
        StatisticScreen(
            statisticViewState = state,
            onAction = onAction,
        )
    }

    @Composable
    override fun mapState(state: Statistic.DataState): StatisticViewState = state.toViewState()

    override fun handleEvent(event: Statistic.Event) {
        when (event) {
            is Statistic.Event.GoBack -> {
                findNavController().navigateUp()
            }
        }
    }

    @Suppress("NonSkippableComposable")
    @Composable
    private fun StatisticScreen(
        statisticViewState: StatisticViewState,
        onAction: (Statistic.Action) -> Unit,
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_statistic),
            backActionClick = { onAction(Statistic.Action.SelectGoBackClick) },
            actionButton = {
                if (statisticViewState.state is StatisticViewState.State.Success) {
                    LoadingButton(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = stringResource(R.string.action_product_statistic_load),
                        isLoading = statisticViewState.state.loadingStatistic,
                        onClick = {
                            onAction(Statistic.Action.LoadStatisticClick)
                        },
                    )
                }
            },
        ) {
            when (statisticViewState.state) {
                StatisticViewState.State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
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
                            hint = stringResource(R.string.msg_common_cafe),
                            label = statisticViewState.state.cafeAddress,
                        )

                        NavigationTextCard(
                            modifier =
                                Modifier
                                    .padding(vertical = 8.dp)
                                    .padding(horizontal = 16.dp),
                            labelText = stringResource(R.string.msg_common_period),
                            valueText = statisticViewState.state.period,
                            onClick = {
                                onAction(Statistic.Action.SelectTimeIntervalClick)
                            },
                        )

                        if (!statisticViewState.state.loadingStatistic) {
                            StatisticSuccessScreen(
                                state = statisticViewState.state,
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
                    bottom = AdminTheme.dimensions.scrollScreenBottomSpace,
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
            title = stringResource(R.string.title_statistic_select_time_interval),
            isShown = state.timeIntervalListUI.isShown,
            onDismissRequest = {
                onAction(Statistic.Action.CloseTimeIntervalListBottomSheet)
            },
            content = {
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
            },
        )
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
    @Preview(showSystemUi = true)
    @Composable
    private fun StatisticScreenPreview() {
        AdminTheme {
            StatisticScreen(
                statisticViewState =
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
            )
        }
    }
}
