package com.bunbeauty.fooddeliveryadmin.screen.statistic

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentStatisticBinding
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.fooddeliveryadmin.screen.option_list.OptionListBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatisticFragment : BaseFragment<FragmentStatisticBinding>() {

    override val viewModel: StatisticViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            viewModel.statisticState.startedObserve { statisticState ->
                fragmentStatisticLpiLoading.isVisible = statisticState.isLoading
                fragmentStatisticNcvCafe.cardText = statisticState.selectedCafe?.title
                fragmentStatisticNcvInterval.cardText = when (statisticState.timeInterval) {
                    TimeInterval.DAY -> resources.getString(R.string.msg_statistic_day_interval)
                    TimeInterval.WEEK -> resources.getString(R.string.msg_statistic_week_interval)
                    TimeInterval.MONTH -> resources.getString(R.string.msg_statistic_month_interval)
                }
            }
            fragmentStatisticNcvCafe.setOnClickListener {
                lifecycleScope.launch {
                    val result = OptionListBottomSheet.show(
                        parentFragmentManager,
                        resources.getString(R.string.title_statistic_select_cafe),
                        viewModel.statisticState.value.cafeList.map { cafe ->
                            Option(
                                id = cafe.uuid,
                                title = cafe.title
                            )
                        }
                    )
                    viewModel.setCafe(result)
                }

            }
            fragmentStatisticNcvInterval.setOnClickListener {
                // go to interval dialog
            }
            fragmentStatisticBtnLoad.setOnClickListener {
                viewModel.loadStatistic()
            }
        }


//        binding.run {
//            viewModel.cafeAddress.onEach { cafeAddress ->
//                fragmentStatisticMcvAddress.cardText = cafeAddress.title
//            }.startedLaunch(viewLifecycleOwner)
//            viewModel.period.onEach { period ->
//                fragmentStatisticMcvPeriod.cardText = period.title
//            }.startedLaunch(viewLifecycleOwner)
//            fragmentStatisticMcvAddress.setOnClickListener {
//                viewModel.goToAddressList()
//            }
//            fragmentStatisticMcvPeriod.setOnClickListener {
//                viewModel.goToPeriodList()
//            }
//
//            val itemAdapter = ItemAdapter<StatisticItem>()
//            val fastAdapter = FastAdapter.with(itemAdapter)
//            fragmentStatisticRvList.adapter = fastAdapter
//            fastAdapter.onClickListener = { _, _, statisticItem, _ ->
//                viewModel.goToStatisticDetails(statisticItem.statisticItemModel)
//                false
//            }
//
//            reloadButton.setOnClickListener {
//                viewModel.getStatistic()
//            }
//
//            setFragmentResultListener(CAFE_ADDRESS_REQUEST_KEY) { _, bundle ->
//                bundle.getParcelable<CafeAddress>(SELECTED_CAFE_ADDRESS_KEY)?.let { cafeAddress ->
//                    viewModel.setCafeAddress(cafeAddress)
//                }
//            }
//            setFragmentResultListener(PERIOD_REQUEST_KEY) { _, bundle ->
//                bundle.getParcelable<Period>(SELECTED_PERIOD_KEY)?.let { period ->
//                    viewModel.setPeriod(period)
//                }
//            }
//
//            viewModel.statisticState.onEach { state ->
//                when (state) {
//                    is State.Loading -> {
//                        fragmentStatisticRvList.gone()
//                        fragmentStatisticLpiLoading.visible()
//                        reloadButton.gone()
//                    }
//                    is State.Empty -> {
//                        fragmentStatisticLpiLoading.invisible()
//                        reloadButton.gone()
//                    }
//                    is State.Success -> {
//                        val items = state.data.map { statisticItemModel ->
//                            StatisticItem(statisticItemModel)
//                        }
//                        itemAdapter.set(items)
//                        fragmentStatisticRvList.visible()
//                        fragmentStatisticLpiLoading.invisible()
//                        reloadButton.gone()
//                    }
//                    is State.Error -> {
//                        fragmentStatisticLpiLoading.invisible()
//                        reloadButton.visible()
//                    }
//                }
//            }.startedLaunch(viewLifecycleOwner)
//        }
//
//        viewModel.navigation.onEach { navigationEvent ->
//            when (navigationEvent) {
//                is StatisticNavigationEvent.ToStatisticDetails ->
//                    router.navigate(
//                        StatisticFragmentDirections.toStatisticDetailsFragment(
//                            navigationEvent.statistic
//                        )
//                    )
//                is StatisticNavigationEvent.ToCafeAddressList ->
//                    router.navigate(StatisticFragmentDirections.toListBottomSheet(navigationEvent.listData))
//                is StatisticNavigationEvent.ToPeriodList ->
//                    router.navigate(StatisticFragmentDirections.toListBottomSheet(navigationEvent.listData))
//                else -> Unit
//            }
//        }.startedLaunch(viewLifecycleOwner)
    }
}