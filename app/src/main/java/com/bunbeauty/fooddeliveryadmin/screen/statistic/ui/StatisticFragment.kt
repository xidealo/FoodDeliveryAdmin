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
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationTextCard
import com.bunbeauty.fooddeliveryadmin.compose.element.selectable.SelectableItem
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.presentation.feature.statistic.Statistic
import com.bunbeauty.presentation.feature.statistic.StatisticViewModel
import kotlinx.collections.immutable.persistentListOf

class StatisticFragment :
    BaseComposeFragment<Statistic.DataState, StatisticViewState, Statistic.Action, Statistic.Event>() {

    override val viewModel: StatisticViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAction(
            Statistic.Action.Init
        )
    }

    @Composable
    override fun Screen(state: StatisticViewState, onAction: (Statistic.Action) -> Unit) {
        StatisticScreen(
            statisticViewState = state,
            onAction = onAction
        )
    }

    @Composable
    override fun mapState(state: Statistic.DataState): StatisticViewState {
        return state.toViewState()
    }

    override fun handleEvent(event: Statistic.Event) {
        when (event) {
            is Statistic.Event.GoBack -> {
                findNavController().navigateUp()
            }
        }
    }

    @Composable
    private fun StatisticScreen(
        statisticViewState: StatisticViewState,
        onAction: (Statistic.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_statistic),
            backActionClick = { onAction(Statistic.Action.SelectGoBackClick) },
            actionButton = {
                LoadingButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(R.string.action_product_statistic_load),
                    isLoading = statisticViewState.isLoading,
                    onClick = {
                        onAction(Statistic.Action.LoadStatisticClick)
                    }
                )
            }
        ) {
            Column {
                NavigationTextCard(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp),
                    labelText = stringResource(R.string.msg_common_cafe),
                    valueText = statisticViewState.selectedCafe,
                    onClick = {
                        onAction(Statistic.Action.SelectCafeClick)
                    }
                )

                NavigationTextCard(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(horizontal = 16.dp),
                    labelText = stringResource(R.string.msg_common_period),
                    valueText = statisticViewState.period,
                    onClick = {
                        onAction(Statistic.Action.SelectTimeIntervalClick)
                    }
                )

                when {
                    statisticViewState.hasError -> {
                        ErrorScreen(
                            mainTextId = R.string.error_common_loading_failed,
                            isLoading = statisticViewState.isLoading
                        ) {
                            onAction(Statistic.Action.LoadStatisticClick)
                        }
                    }

                    else -> {
                        StatisticSuccessScreen(state = statisticViewState, onAction = onAction)
                    }
                }
            }
        }
    }

    @Composable
    private fun StatisticSuccessScreen(
        state: StatisticViewState,
        onAction: (Statistic.Action) -> Unit
    ) {
        val listState = rememberLazyListState()

        LazyColumn(
            contentPadding = PaddingValues(
                top = 8.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState
        ) {
            items(
                items = state.statisticList,
                key = { statisticItemModel ->
                    statisticItemModel.startMillis
                }
            ) { statisticItemModel ->
                StatisticItem(statisticItemModel)
            }
        }

        LaunchedEffect(state.statisticList) {
            listState.animateScrollToItem(0)
        }

        CafeListBottomSheet(state = state, onAction = onAction)
        TimeIntervalListBottomSheet(state = state, onAction = onAction)
    }

    @Composable
    private fun TimeIntervalListBottomSheet(
        state: StatisticViewState,
        onAction: (Statistic.Action) -> Unit
    ) {
        AdminModalBottomSheet(
            title = stringResource(R.string.title_statistic_select_time_interval),
            isShown = state.timeIntervalListUI.isShown,
            onDismissRequest = {
                onAction(Statistic.Action.CloseTimeIntervalListBottomSheet)
            }
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                state.timeIntervalListUI.timeIntervalList.forEach { timeInterval ->
                    SelectableItem(
                        title = timeInterval.timeInterval,
                        clickable = true,
                        elevated = false,
                        onClick = {
                            onAction(
                                Statistic.Action.SelectedTimeInterval(
                                    timeInterval = timeInterval.timeIntervalType
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun CafeListBottomSheet(
        state: StatisticViewState,
        onAction: (Statistic.Action) -> Unit
    ) {
        AdminModalBottomSheet(
            title = stringResource(R.string.title_statistic_select_time_interval),
            isShown = state.cafeListUI.isShown,
            onDismissRequest = {
                onAction(Statistic.Action.CloseCafeListBottomSheet)
            }
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                state.cafeListUI.cafeList.forEach { cafe ->
                    SelectableItem(
                        title = cafe.name,
                        clickable = true,
                        elevated = false,
                        onClick = {
                            onAction(
                                Statistic.Action.SelectedCafe(
                                    cafeUuid = cafe.uuid
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun StatisticItem(statisticItemModel: StatisticViewState.StatisticItemModel) {
        AdminCard(
            modifier = Modifier.fillMaxWidth(),
            clickable = false
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = statisticItemModel.date,
                    style = AdminTheme.typography.titleSmall,
                    color = AdminTheme.colors.main.onSurface
                )
                Column(
                    modifier = Modifier
                        .padding(
                            start = 8.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = statisticItemModel.count,
                        style = AdminTheme.typography.bodySmall,
                        color = AdminTheme.colors.main.onSurface
                    )
                    Text(
                        modifier = Modifier
                            .padding(
                                top = 4.dp
                            ),
                        text = statisticItemModel.proceeds,
                        style = AdminTheme.typography.bodyMedium.bold,
                        color = AdminTheme.colors.main.onSurface
                    )
                }
            }
        }
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun StatisticScreenPreview() {
        AdminTheme {
            StatisticScreen(
                statisticViewState = StatisticViewState(
                    statisticList = persistentListOf(
                        StatisticViewState.StatisticItemModel(
                            startMillis = 3064,
                            period = "апрель",
                            count = "Заказов: 20",
                            proceeds = "2000 $",
                            date = "ssss"
                        ),
                        StatisticViewState.StatisticItemModel(
                            startMillis = 3064,
                            period = "май",
                            count = "Заказов: 387",
                            proceeds = "128234 $",
                            date = "ssss"
                        )
                    ),
                    selectedCafe = "Все кафе",
                    period = "По годам",
                    isLoading = false,
                    hasError = false,
                    cafeListUI = CafeListUI(isShown = false, cafeList = persistentListOf()),
                    timeIntervalListUI = TimeIntervalListUI(
                        isShown = false,
                        timeIntervalList = persistentListOf()
                    )
                ),
                onAction = {}
            )
        }
    }
}
