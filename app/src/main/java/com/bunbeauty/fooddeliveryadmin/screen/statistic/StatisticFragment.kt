package com.bunbeauty.fooddeliveryadmin.screen.statistic

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentStatisticBinding
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.fooddeliveryadmin.screen.option_list.OptionListBottomSheet
import com.bunbeauty.fooddeliveryadmin.util.addSpaceItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StatisticFragment : BaseFragment<FragmentStatisticBinding>() {

    @Inject
    lateinit var statisticAdapter: StatisticAdapter

    override val viewModel: StatisticViewModel by viewModels()

    private var cafeListBottomSheetJob: Job? = null
    private var timeIntervalListBottomSheetJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
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

    private fun handleEvents(eventList: List<StatisticState.Event>) {
        eventList.forEach { event ->
            when (event) {
                is StatisticState.Event.OpenCafeListEvent -> {
                    openCafeListBottom(event.cafeList)
                }
                is StatisticState.Event.OpenTimeIntervalListEvent -> {
                    openTimeIntervals(event.timeIntervalList)
                }
            }
        }
        viewModel.consumeEvents(eventList)
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