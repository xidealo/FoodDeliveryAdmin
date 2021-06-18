package com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic

import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetStatisticDetailsBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.presentation.SelectedStatisticViewModel
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic.StatisticDetailsBottomSheetArgs.fromBundle

class StatisticDetailsBottomSheet :
    BaseBottomSheetDialog<BottomSheetStatisticDetailsBinding, SelectedStatisticViewModel>() {

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val statisticItem = fromBundle(requireArguments()).statisticItem
        viewDataBinding.bottomSheetStatisticDetailsTvTitle.text = statisticItem.period
        viewDataBinding.bottomSheetStatisticDetailsTvProducts.text =
            viewModel.getStatisticInfo(statisticItem.orderList)
    }
}