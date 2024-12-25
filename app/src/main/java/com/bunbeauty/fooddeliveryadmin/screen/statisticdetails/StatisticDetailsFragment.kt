package com.bunbeauty.fooddeliveryadmin.screen.statisticdetails

import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.bunbeauty.fooddeliveryadmin.coreui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentStatisticDetailsBinding
import com.bunbeauty.presentation.viewmodel.statistic.StatisticDetailsViewModel


class StatisticDetailsFragment : BaseFragment<FragmentStatisticDetailsBinding>() {

    override val viewModel: StatisticDetailsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            fragmentStatisticDetailsBtnBack.setOnClickListener {
            }
            fragmentStatisticDetailsTvPeriod.text = viewModel.period
            fragmentStatisticDetailsTvTotalProceedsValue.text = viewModel.proceeds
            fragmentStatisticDetailsTvTotalCountValue.text = viewModel.orderCount
            fragmentStatisticDetailsTvTotalAverageCheckValue.text = viewModel.averageCheck
        }
    }
}
