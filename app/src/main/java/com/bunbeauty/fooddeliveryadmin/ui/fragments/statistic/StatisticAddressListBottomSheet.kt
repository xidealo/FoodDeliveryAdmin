package com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.common.Constants.ADDRESS_REQUEST_KEY
import com.bunbeauty.common.Constants.SELECTED_ADDRESS_KEY
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetAddressListBinding
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.presentation.statistic.StatisticAddressListViewModel
import com.bunbeauty.fooddeliveryadmin.ui.items.AddressItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class StatisticAddressListBottomSheet: BaseBottomSheetDialog<BottomSheetAddressListBinding>() {

    override val viewModel: StatisticAddressListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter = ItemAdapter<AddressItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.bottomSheetAddressListRvList.adapter = fastAdapter
        fastAdapter.onClickListener = { _, _, addressItem, _ ->
            val bundle = bundleOf(SELECTED_ADDRESS_KEY to addressItem)
            setFragmentResult(ADDRESS_REQUEST_KEY, bundle)
            viewModel.goBack()
            false
        }
        viewModel.addressListState.onEach { state ->
            when (state) {
                is State.Success -> {
                    itemAdapter.set(state.data)
                }
                else -> {
                }
            }
        }.startedLaunch(viewLifecycleOwner)

        viewModel.getCafeList()
    }
}