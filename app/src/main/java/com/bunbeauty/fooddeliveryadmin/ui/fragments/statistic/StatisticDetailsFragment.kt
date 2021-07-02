package com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentStatisticDetailsBinding
import com.bunbeauty.fooddeliveryadmin.presentation.statistic.StatisticDetailsViewModel
import com.bunbeauty.fooddeliveryadmin.ui.items.ProductStatisticItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticDetailsFragment : BaseFragment<FragmentStatisticDetailsBinding>() {

    override val viewModel: StatisticDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.fragmentStatisticDetailsBtnBack.setOnClickListener {
            viewModel.goBack()
        }
        viewDataBinding.fragmentStatisticDetailsTvPeriod.text = viewModel.period
        viewDataBinding.fragmentStatisticDetailsTvTotalProceedsValue.text = viewModel.proceeds
        viewDataBinding.fragmentStatisticDetailsTvTotalCountValue.text = viewModel.orderCount
        viewDataBinding.fragmentStatisticDetailsTvTotalAverageCheckValue.text = viewModel.averageCheck

        val itemAdapter = ItemAdapter<ProductStatisticItem>().apply {
            set(viewModel.productStatisticList)
        }
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.fragmentStatisticDetailsRvList.adapter = fastAdapter
    }
}