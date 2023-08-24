package com.bunbeauty.fooddeliveryadmin.screen.statisticdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.coreui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentStatisticDetailsBinding
import com.bunbeauty.presentation.viewmodel.statistic.StatisticDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticDetailsFragment : BaseFragment<FragmentStatisticDetailsBinding>() {

    override val viewModel: StatisticDetailsViewModel by viewModels()

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
