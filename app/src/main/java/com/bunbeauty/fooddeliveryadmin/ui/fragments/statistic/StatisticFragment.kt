package com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.bunbeauty.common.Constants.CAFE_ADDRESS_REQUEST_KEY
import com.bunbeauty.common.Constants.PERIOD_REQUEST_KEY
import com.bunbeauty.common.Constants.SELECTED_CAFE_ADDRESS_KEY
import com.bunbeauty.common.Constants.SELECTED_PERIOD_KEY
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentStatisticBinding
import com.bunbeauty.fooddeliveryadmin.extensions.gone
import com.bunbeauty.fooddeliveryadmin.extensions.invisible
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.extensions.visible
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.items.StatisticItem
import com.bunbeauty.presentation.model.list.CafeAddress
import com.bunbeauty.presentation.model.list.Period
import com.bunbeauty.presentation.navigation_event.StatisticNavigationEvent
import com.bunbeauty.presentation.state.State
import com.bunbeauty.presentation.view_model.statistic.StatisticViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class StatisticFragment : BaseFragment<FragmentStatisticBinding>() {

    override val viewModel: StatisticViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            viewModel.cafeAddress.onEach { cafeAddress ->
                fragmentStatisticMcvAddress.cardText = cafeAddress.title
            }.startedLaunch(viewLifecycleOwner)
            viewModel.period.onEach { period ->
                fragmentStatisticMcvPeriod.cardText = period.title
            }.startedLaunch(viewLifecycleOwner)
            fragmentStatisticMcvAddress.setOnClickListener {
                viewModel.goToAddressList()
            }
            fragmentStatisticMcvPeriod.setOnClickListener {
                viewModel.goToPeriodList()
            }

            val itemAdapter = ItemAdapter<StatisticItem>()
            val fastAdapter = FastAdapter.with(itemAdapter)
            fragmentStatisticRvList.adapter = fastAdapter
            fastAdapter.onClickListener = { _, _, statisticItem, _ ->
                viewModel.goToStatisticDetails(statisticItem.statisticItemModel)
                false
            }

            setFragmentResultListener(CAFE_ADDRESS_REQUEST_KEY) { _, bundle ->
                bundle.getParcelable<CafeAddress>(SELECTED_CAFE_ADDRESS_KEY)?.let { cafeAddress ->
                    viewModel.setCafeAddress(cafeAddress)
                }
            }
            setFragmentResultListener(PERIOD_REQUEST_KEY) { _, bundle ->
                bundle.getParcelable<Period>(SELECTED_PERIOD_KEY)?.let { period ->
                    viewModel.setPeriod(period)
                }
            }

            viewModel.statisticState.onEach { state ->
                when (state) {
                    is State.Loading -> {
                        fragmentStatisticRvList.gone()
                        fragmentStatisticLpiLoading.visible()
                    }
                    is State.Empty -> {
                        fragmentStatisticLpiLoading.invisible()
                    }
                    is State.Success -> {
                        val items = state.data.map { statisticItemModel ->
                            StatisticItem(statisticItemModel)
                        }
                        itemAdapter.set(items)
                        fragmentStatisticRvList.visible()
                        fragmentStatisticLpiLoading.invisible()
                    }
                    is State.Error -> {
                        fragmentStatisticLpiLoading.invisible()
                    }
                }
            }.startedLaunch(viewLifecycleOwner)
        }

        viewModel.navigation.onEach { navigationEvent ->
            when (navigationEvent) {
                is StatisticNavigationEvent.ToStatisticDetails ->
                    router.navigate(StatisticFragmentDirections.toStatisticDetailsFragment(navigationEvent.statistic))
                is StatisticNavigationEvent.ToCafeAddressList ->
                    router.navigate(StatisticFragmentDirections.toListBottomSheet(navigationEvent.listData))
                is StatisticNavigationEvent.ToPeriodList ->
                    router.navigate(StatisticFragmentDirections.toListBottomSheet(navigationEvent.listData))
                else -> Unit
            }
        }.startedLaunch(viewLifecycleOwner)
    }
}