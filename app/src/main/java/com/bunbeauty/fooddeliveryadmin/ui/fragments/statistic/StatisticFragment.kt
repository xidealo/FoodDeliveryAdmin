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
import com.bunbeauty.fooddeliveryadmin.extensions.visible
import com.bunbeauty.presentation.view_model.state.State
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.items.StatisticItem
import com.bunbeauty.presentation.list.CafeAddress
import com.bunbeauty.presentation.list.Period
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticFragment : BaseFragment<FragmentStatisticBinding>() {

    override val viewModel: com.bunbeauty.presentation.view_model.statistic.StatisticViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            fragmentStatisticMcvAddress.cardText = viewModel.selectedCafeAddress.title
            fragmentStatisticMcvPeriod.cardText = viewModel.selectedPeriod.title
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
                viewModel.goToStatisticDetails(statisticItem.statistic)
                false
            }

            setFragmentResultListener(CAFE_ADDRESS_REQUEST_KEY) { _, bundle ->
                bundle.getParcelable<CafeAddress>(SELECTED_CAFE_ADDRESS_KEY)?.let { cafeAddress ->
                    fragmentStatisticMcvAddress.cardText = cafeAddress.title
                    viewModel.selectedCafeAddress = cafeAddress
                    viewModel.getStatistic(viewModel.selectedCafeAddress, viewModel.selectedPeriod)
                }
            }
            setFragmentResultListener(PERIOD_REQUEST_KEY) { _, bundle ->
                bundle.getParcelable<Period>(SELECTED_PERIOD_KEY)?.let { period ->
                    fragmentStatisticMcvPeriod.cardText = period.title
                    viewModel.selectedPeriod = period
                    viewModel.getStatistic(viewModel.selectedCafeAddress, viewModel.selectedPeriod)
                }
            }

            viewModel.statisticState.onEach { state ->
                when (state) {
                    is com.bunbeauty.presentation.view_model.state.State.Loading -> {
                        fragmentStatisticRvList.gone()
                        fragmentStatisticLpiLoading.visible()
                    }
                    is com.bunbeauty.presentation.view_model.state.State.Empty -> {
                        fragmentStatisticLpiLoading.invisible()
                    }
                    is com.bunbeauty.presentation.view_model.state.State.Success -> {
                        itemAdapter.set(state.data)
                        fragmentStatisticRvList.visible()
                        fragmentStatisticLpiLoading.invisible()
                    }
                    is com.bunbeauty.presentation.view_model.state.State.Error -> {
                        fragmentStatisticLpiLoading.invisible()
                    }
                }
            }.startedLaunch(viewLifecycleOwner)
        }

        viewModel.getStatistic(viewModel.selectedCafeAddress, viewModel.selectedPeriod)
    }
}