package com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.bunbeauty.data.enums.Period
import com.bunbeauty.fooddeliveryadmin.Constants.PERIOD_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.Constants.SELECTED_PERIOD_KEY
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetStatisticPeriodBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.presentation.EmptyViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.PeriodItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

class StatisticPeriodBottomSheet :
    BaseBottomSheetDialog<BottomSheetStatisticPeriodBinding, EmptyViewModel>() {

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter = ItemAdapter<PeriodItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.bottomSheetStatisticPeriodRvList.adapter = fastAdapter
        itemAdapter.set(
            Period.values().map { period ->
                PeriodItem(period.text)
            }
        )
        fastAdapter.onClickListener = { _, _, addressItem, _ ->
            val bundle = bundleOf(SELECTED_PERIOD_KEY to addressItem)
            setFragmentResult(PERIOD_REQUEST_KEY, bundle)
            dismiss()
            false
        }

    }
}