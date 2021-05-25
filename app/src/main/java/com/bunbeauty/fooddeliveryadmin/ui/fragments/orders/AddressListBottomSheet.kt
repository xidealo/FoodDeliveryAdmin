package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.common.State
import com.bunbeauty.common.extensions.launchWhenStarted
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetAddressListBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.presentation.AddressListViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.AddressItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.onEach

class AddressListBottomSheet :
    BaseBottomSheetDialog<BottomSheetAddressListBinding, AddressListViewModel>() {

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter = ItemAdapter<AddressItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.bottomSheetAddressListRvList.adapter = fastAdapter
        fastAdapter.onClickListener = { _, _, addressItem, _ ->
            viewModel.saveCafeId(addressItem.cafeId)
            dismiss()
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
        }.launchWhenStarted(lifecycleScope)
    }
}