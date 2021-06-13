package com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.common.State
import com.bunbeauty.common.extensions.launchWhenStarted
import com.bunbeauty.fooddeliveryadmin.Constants.ADDRESS_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.Constants.SELECTED_ADDRESS_KEY
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetAddressListBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.presentation.StatisticAddressListViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.AddressItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.onEach

class StatisticAddressListBottomSheet:
    BaseBottomSheetDialog<BottomSheetAddressListBinding, StatisticAddressListViewModel>() {

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter = ItemAdapter<AddressItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.bottomSheetAddressListRvList.adapter = fastAdapter
        fastAdapter.onClickListener = { _, _, addressItem, _ ->
            val bundle = bundleOf(SELECTED_ADDRESS_KEY to addressItem)
            setFragmentResult(ADDRESS_REQUEST_KEY, bundle)
            dismiss()
            false
        }
        viewModel.addressListState.onEach { state ->
            when(state) {
                is State.Success -> {
                    itemAdapter.set(state.data)
                }
                else -> {}
            }
        }.launchWhenStarted(lifecycleScope)
    }
}