package com.bunbeauty.fooddeliveryadmin.screen.statistic_details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentStatisticDetailsBinding
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.presentation.view_model.statistic.StatisticDetailsViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
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
            val itemAdapter = ItemAdapter<ProductStatisticItem>()
            val items = viewModel.productStatisticList.map { productStatisticItemModel ->
                ProductStatisticItem(productStatisticItemModel)
            }
            itemAdapter.set(items)
            val fastAdapter = FastAdapter.with(itemAdapter)
            fragmentStatisticDetailsRvList.adapter = fastAdapter
        }
    }
}