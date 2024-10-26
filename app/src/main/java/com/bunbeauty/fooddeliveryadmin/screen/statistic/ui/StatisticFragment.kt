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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationTextCard
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.screen.optionlist.OptionListBottomSheet
import com.bunbeauty.presentation.Option
import com.bunbeauty.presentation.feature.statistic.Statistic
import com.bunbeauty.presentation.feature.statistic.StatisticViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatisticFragment :
    BaseComposeFragment<Statistic.DataState, StatisticViewState, Statistic.Action, Statistic.Event>() {

    override val viewModel: StatisticViewModel by viewModels()

    private var cafeListBottomSheetJob: Job? = null
    private var timeIntervalListBottomSheetJob: Job? = null

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
        return StatisticViewState(
            statisticList = state.statisticList.map { statisticItemModel ->
                StatisticViewState.StatisticItemModel(
                    startMillis = statisticItemModel.startMillis,
                    period = statisticItemModel.period,
                    count = resources.getString(
                        R.string.msg_statistic_orders,
                        statisticItemModel.count
                    ),
                    proceeds = statisticItemModel.proceeds,
                    date = statisticItemModel.date
                )
            }.toPersistentList(),
            selectedCafe = state.selectedCafe?.address
                ?: resources.getString(R.string.msg_statistic_all_cafes),
            period = getTimeIntervalName(
                state.selectedTimeInterval
            ),
            isLoading = state.isLoading,
            hasError = state.hasError
        )
    }

    private fun getTimeIntervalName(timeInterval: Statistic.TimeIntervalCode): String {
        return when (timeInterval) {
            Statistic.TimeIntervalCode.DAY -> resources.getString(R.string.msg_statistic_day_interval)
            Statistic.TimeIntervalCode.WEEK -> resources.getString(R.string.msg_statistic_week_interval)
            Statistic.TimeIntervalCode.MONTH -> resources.getString(R.string.msg_statistic_month_interval)
        }
    }

    override fun handleEvent(event: Statistic.Event) {
        when (event) {
            is Statistic.Event.OpenCafeListEvent -> {
                openCafeListBottom(buildOptionList(event))
            }

            is Statistic.Event.OpenTimeIntervalListEvent -> {
                openTimeIntervals(buildOptionList(event))
            }

            is Statistic.Event.GoBack -> {
                findNavController().navigateUp()
            }
        }
    }

    private fun buildOptionList(event: Statistic.Event.OpenCafeListEvent): List<Option> {
        val optionsList = buildList {
            add(
                Option(
                    id = null,
                    title = resources.getString(R.string.msg_statistic_all_cafes)
                )
            )
            event.cafeList.map { cafe ->
                Option(
                    id = cafe.uuid,
                    title = cafe.address
                )
            }.let { cafeAddressList ->
                addAll(cafeAddressList)
            }
        }
        return optionsList
    }

    private fun buildOptionList(event: Statistic.Event.OpenTimeIntervalListEvent): List<Option> {
        return event.timeIntervalList.map { timeInterval ->
            Option(
                id = timeInterval.name,
                title = getTimeIntervalName(timeInterval)
            )
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
                        StatisticSuccessScreen(statisticViewState)
                    }
                }
            }
        }
    }

    @Composable
    private fun StatisticSuccessScreen(statisticViewState: StatisticViewState) {
        LazyColumn(
            contentPadding = PaddingValues(
                top = 8.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = statisticViewState.statisticList,
                key = { statisticItemModel ->
                    statisticItemModel.startMillis
                }
            ) { statisticItemModel ->
                StatisticItem(statisticItemModel)
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
                    hasError = false
                ),
                onAction = {}
            )
        }
    }

    private fun openCafeListBottom(cafeList: List<Option>) {
        val isPossibleToOpen = cafeListBottomSheetJob?.let { job ->
            !job.isActive
        } ?: true
        if (isPossibleToOpen) {
            cafeListBottomSheetJob = lifecycleScope.launch {
                OptionListBottomSheet.show(
                    parentFragmentManager,
                    resources.getString(R.string.title_statistic_select_cafe),
                    cafeList
                )?.let { result ->
                    viewModel.onCafeSelected(result.value)
                }
            }
        }
    }

    private fun openTimeIntervals(timeIntervalList: List<Option>) {
        val isPossibleToOpen = timeIntervalListBottomSheetJob?.let { job ->
            !job.isActive
        } ?: true
        if (isPossibleToOpen) {
            timeIntervalListBottomSheetJob = lifecycleScope.launch {
                OptionListBottomSheet.show(
                    parentFragmentManager,
                    resources.getString(R.string.title_statistic_select_time_interval),
                    timeIntervalList
                )?.value?.let { resultValue ->
                    viewModel.onTimeIntervalSelected(resultValue)
                }
            }
        }
    }
}
