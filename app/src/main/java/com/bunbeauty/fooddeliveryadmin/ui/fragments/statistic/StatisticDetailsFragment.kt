package com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentStatisticDetailsBinding
import com.bunbeauty.fooddeliveryadmin.ui.items.ProductStatisticItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticDetailsFragment : BaseFragment<FragmentStatisticDetailsBinding>() {

    override val viewModel: com.bunbeauty.presentation.view_model.statistic.StatisticDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fragmentStatisticDetailsBtnBack.setOnClickListener {
            viewModel.goBack()
        }
        binding.fragmentStatisticDetailsTvPeriod.text = viewModel.period
        binding.fragmentStatisticDetailsTvTotalProceedsValue.text = viewModel.proceeds
        binding.fragmentStatisticDetailsTvTotalCountValue.text = viewModel.orderCount
        binding.fragmentStatisticDetailsTvTotalAverageCheckValue.text = viewModel.averageCheck

        val itemAdapter = ItemAdapter<ProductStatisticItem>().apply {
            set(viewModel.productStatisticList)
        }
        val fastAdapter = FastAdapter.with(itemAdapter)
        binding.fragmentStatisticDetailsRvList.adapter = fastAdapter
    }
}