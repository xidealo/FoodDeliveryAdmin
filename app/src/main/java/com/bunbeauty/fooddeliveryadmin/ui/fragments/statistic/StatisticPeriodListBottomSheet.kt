package com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.bunbeauty.common.Constants.PERIOD_REQUEST_KEY
import com.bunbeauty.common.Constants.SELECTED_PERIOD_KEY
import com.bunbeauty.domain.enums.Period
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetStatisticPeriodListBinding
import com.bunbeauty.fooddeliveryadmin.presentation.EmptyViewModel
import com.bunbeauty.fooddeliveryadmin.ui.items.PeriodItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatisticPeriodListBottomSheet :
    BaseBottomSheetDialog<BottomSheetStatisticPeriodListBinding>() {

    override val viewModel: EmptyViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter = ItemAdapter<PeriodItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.bottomSheetStatisticPeriodListRvList.adapter = fastAdapter
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