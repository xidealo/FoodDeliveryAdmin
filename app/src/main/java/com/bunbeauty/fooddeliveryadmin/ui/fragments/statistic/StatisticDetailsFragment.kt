package com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic

import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentStatisticDetailsBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.presentation.statistic.StatisticDetailsViewModel
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog

class StatisticDetailsFragment :
    BaseBottomSheetDialog<FragmentStatisticDetailsBinding, StatisticDetailsViewModel>() {

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.getStatisticDetailsComponent().create(this).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.fragmentStatisticDetailsTvPeriod.text = viewModel.period
        viewDataBinding.fragmentStatisticDetailsTvProceedsValue.text = viewModel.proceeds
        viewDataBinding.fragmentStatisticDetailsTvCountValue.text = viewModel.orderCount
        viewDataBinding.fragmentStatisticDetailsTvAverageCheckValue.text = viewModel.averageCheck
    }
}