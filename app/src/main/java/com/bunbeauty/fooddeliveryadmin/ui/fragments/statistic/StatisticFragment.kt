package com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.bunbeauty.common.Constants.ADDRESS_REQUEST_KEY
import com.bunbeauty.common.Constants.PERIOD_REQUEST_KEY
import com.bunbeauty.common.Constants.SELECTED_ADDRESS_KEY
import com.bunbeauty.common.Constants.SELECTED_PERIOD_KEY
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentStatisticBinding
import com.bunbeauty.fooddeliveryadmin.extensions.gone
import com.bunbeauty.fooddeliveryadmin.extensions.invisible
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.extensions.visible
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.presentation.statistic.StatisticViewModel
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.items.AddressItem
import com.bunbeauty.fooddeliveryadmin.ui.items.PeriodItem
import com.bunbeauty.fooddeliveryadmin.ui.items.StatisticItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class StatisticFragment : BaseFragment<FragmentStatisticBinding>() {

    override val viewModel: StatisticViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.fragmentStatisticMcvAddress.cardText = viewModel.selectedAddressItem.address
        viewDataBinding.fragmentStatisticMcvPeriod.cardText = viewModel.selectedPeriodItem.period
        viewDataBinding.fragmentStatisticMcvAddress.setOnClickListener {
            viewModel.goToAddressList()
        }
        viewDataBinding.fragmentStatisticMcvPeriod.setOnClickListener {
            viewModel.goToPeriodList()
        }

        val itemAdapter = ItemAdapter<StatisticItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.fragmentStatisticRvList.adapter = fastAdapter
        fastAdapter.onClickListener = { _, _, statisticItem, _ ->
            viewModel.goToStatisticDetails(statisticItem.statistic)
            false
        }

        setFragmentResultListener(ADDRESS_REQUEST_KEY) { _, bundle ->
            bundle.getParcelable<AddressItem>(SELECTED_ADDRESS_KEY)?.let { addressItem ->
                viewDataBinding.fragmentStatisticMcvAddress.cardText = addressItem.address
                viewModel.selectedAddressItem = addressItem
                viewModel.getStatistic(
                    viewModel.selectedAddressItem.cafeUuid,
                    viewModel.selectedPeriodItem.period
                )
            }
        }
        setFragmentResultListener(PERIOD_REQUEST_KEY) { _, bundle ->
            bundle.getParcelable<PeriodItem>(SELECTED_PERIOD_KEY)?.let { periodItem ->
                viewDataBinding.fragmentStatisticMcvPeriod.cardText = periodItem.period
                viewModel.selectedPeriodItem = periodItem
                viewModel.getStatistic(
                    viewModel.selectedAddressItem.cafeUuid,
                    viewModel.selectedPeriodItem.period
                )
            }
        }

        viewModel.statisticState.onEach { state ->
            when (state) {
                is State.Loading -> {
                    viewDataBinding.fragmentStatisticRvList.gone()
                    viewDataBinding.fragmentStatisticLpiLoading.visible()
                }
                is State.Empty -> {
                    viewDataBinding.fragmentStatisticLpiLoading.invisible()
                }
                is State.Success -> {
                    itemAdapter.set(state.data)
                    viewDataBinding.fragmentStatisticRvList.visible()
                    viewDataBinding.fragmentStatisticLpiLoading.invisible()
                }
                is State.Error -> {
                    viewDataBinding.fragmentStatisticLpiLoading.invisible()
                }
            }
        }.startedLaunch(viewLifecycleOwner)

        viewModel.getStatistic(
            viewModel.selectedAddressItem.cafeUuid,
            viewModel.selectedPeriodItem.period
        )
    }
}