package com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic

import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentStatisticDetailsBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.presentation.statistic.StatisticDetailsViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.CartProductItem
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.ProductStatisticItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

class StatisticDetailsFragment :
    BaseBottomSheetDialog<FragmentStatisticDetailsBinding, StatisticDetailsViewModel>() {

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.getStatisticDetailsComponent().create(this).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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