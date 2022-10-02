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
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StatisticFragment : BaseFragment<FragmentStatisticBinding>() {

    @Inject
    lateinit var statisticAdapter: StatisticAdapter

    override val viewModel: StatisticViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            viewModel.statisticState.startedObserve { statisticState ->
                fragmentStatisticLpiLoading.isInvisible = !statisticState.isLoading
                fragmentStatisticTvCafe.text = statisticState.selectedCafe?.title
                fragmentStatisticTvInterval.text =
                    viewModel.getIntervalName(statisticState.selectedTimeInterval)
                statisticAdapter.submitList(statisticState.statisticList)
                if (statisticState.isCafesOpen) {
                    openCafes(statisticState.cafeList)
                    viewModel.cafesOpened()
                }
                if (statisticState.isTimeIntervalsOpen) {
                    openTimeIntervals()
                    viewModel.timeIntervalsOpened()
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
        }
    }

    private fun openCafes(cafeList: List<Cafe>) {
        lifecycleScope.launch {
            OptionListBottomSheet.show(
                parentFragmentManager,
                resources.getString(R.string.title_statistic_select_cafe),
                cafeList.map { cafe ->
                    Option(
                        id = cafe.uuid,
                        title = cafe.title
                    )
                }
            ).let { result ->
                viewModel.setCafe(result)
            }
        }
    }

    private fun openTimeIntervals() {
        lifecycleScope.launch {
            OptionListBottomSheet.show(
                parentFragmentManager,
                resources.getString(R.string.title_statistic_select_time_interval),
                TimeInterval.values().map { timeInterval ->
                    Option(
                        id = timeInterval.name,
                        title = viewModel.getIntervalName(timeInterval)
                    )
                }
            )?.let { result ->
                viewModel.setTimeInterval(result)
            }
        }
    }
}