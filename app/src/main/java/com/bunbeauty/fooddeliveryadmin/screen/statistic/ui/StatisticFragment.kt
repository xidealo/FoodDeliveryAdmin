package com.bunbeauty.fooddeliveryadmin.screen.statistic.ui

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationTextCard
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.coreui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
import com.bunbeauty.fooddeliveryadmin.screen.error.ErrorDialog
import com.bunbeauty.fooddeliveryadmin.screen.optionlist.OptionListBottomSheet
import com.bunbeauty.presentation.feature.statistic.StatisticViewModel
import com.bunbeauty.presentation.Option
import com.bunbeauty.presentation.feature.editcafe.EditCafe
import com.bunbeauty.presentation.feature.statistic.Statistic
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatisticFragment :
    BaseComposeFragment<Statistic.ViewDataState, StatisticViewState, Statistic.Action, Statistic.Event>() {

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

    override fun mapState(state: Statistic.ViewDataState): StatisticViewState {
        return StatisticViewState(
            statisticList = state.statisticList.toPersistentList(),
            selectedCafe = state.selectedCafe?.address ?: "",
            period = state.selectedTimeInterval?.name ?: ""
        )
    }

    override fun handleEvent(event: Statistic.Event) {
        when (event) {
            is Statistic.Event.OpenCafeListEvent -> {
                openCafeListBottom(event.cafeList)
            }

            is Statistic.Event.OpenTimeIntervalListEvent -> {
                openTimeIntervals(event.timeIntervalList)
            }

            is Statistic.Event.ShowError -> {
                lifecycleScope.launch {
                    ErrorDialog.show(childFragmentManager).let {
                        viewModel.onRetryClicked(event.retryAction)
                    }
                }
            }

            Statistic.Event.GoBack -> {

            }
        }
    }

    @Composable
    private fun StatisticScreen(
        statisticViewState: StatisticViewState,
        onAction: (Statistic.Action) -> Unit,
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_statistic),
            actionButton = {
                LoadingButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textStringId = R.string.action_product_statistic_load,
                    onClick = {
                        onAction(Statistic.Action.LoadStatisticClick)
                    },
                    isLoading = false
                )
            }
        ) {
            Column {
                NavigationTextCard(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 16.dp),
                    label = statisticViewState.selectedCafe,
                    onClick = {
                        onAction(Statistic.Action.SelectCafeClick)
                    }
                )

                NavigationTextCard(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(horizontal = 16.dp),
                    label = statisticViewState.period,
                    onClick = {

                    }
                )

                LazyColumn(
                    contentPadding = PaddingValues(
                        vertical = 12.dp,
                        horizontal = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(statisticViewState.statisticList) { statisticItemModel ->
                        AdminCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Text(
                                modifier = Modifier.padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp
                                ),
                                text = statisticItemModel.period
                            )

                            Text(
                                modifier = Modifier.padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp
                                ),
                                text = statisticItemModel.proceeds
                            )
                            Text(
                                modifier = Modifier.padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp
                                ),
                                text = statisticItemModel.count
                            )
                        }
                    }
                }
            }
        }
    }

    @Preview
    @Composable
    private fun StatisticScreenPreview() {
        AdminTheme {
            StatisticScreen(
                statisticViewState = StatisticViewState(
                    statisticList = persistentListOf(
                        Statistic.ViewDataState.StatisticItemModel(
                            startMillis = 3064,
                            period = "suas",
                            count = "quaestio",
                            proceeds = "oratio"
                        ),
                        Statistic.ViewDataState.StatisticItemModel(
                            startMillis = 3064,
                            period = "sssss",
                            count = "quaestio",
                            proceeds = "oratio"
                        )
                    ),
                    selectedCafe = "Все кафе",
                    period = "По годам",
                ),
                onAction = {}
            )
        }
    }

    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    else -> {
                        false
                    }
                }
            }
            fragmentStatisticRvList.adapter = statisticAdapter
            fragmentStatisticRvList.addSpaceItemDecorator(R.dimen.very_small_margin)
            fragmentStatisticMcvCafe.setOnClickListener {
                viewModel.onCafeClicked()
            }
            fragmentStatisticMcvInterval.setOnClickListener {
                viewModel.onTimeIntervalClicked()
            }
            fragmentStatisticBtnLoad.setOnClickListener {
                viewModel.loadStatistic()
            }

            viewModel.statisticState.collectWithLifecycle { statisticState ->
                fragmentStatisticLpiLoading.isInvisible = !statisticState.isLoading
                fragmentStatisticBtnLoad.isEnabled = !statisticState.isLoading
                fragmentStatisticTvCafe.text = statisticState.selectedCafe?.address
                fragmentStatisticTvInterval.text = statisticState.selectedTimeInterval?.name
                statisticAdapter.submitList(statisticState.statisticList)
                handleEvents(statisticState.eventList)
            }
        }
    }
*/

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
