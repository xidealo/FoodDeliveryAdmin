package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.bunbeauty.fooddeliveryadmin.presentation.state.State
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetAddressListBinding
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.presentation.order.AddressListViewModel
import com.bunbeauty.fooddeliveryadmin.ui.items.AddressItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddressListBottomSheet : BaseBottomSheetDialog<BottomSheetAddressListBinding>() {

    override val viewModel: AddressListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter = ItemAdapter<AddressItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        binding.bottomSheetAddressListRvList.adapter = fastAdapter
        fastAdapter.onClickListener = { _, _, addressItem, _ ->
            viewModel.saveCafeId(addressItem.cafeUuid)
            false
        }
        viewModel.addressListState.onEach { state ->
            when (state) {
                is State.Success -> {
                    itemAdapter.set(state.data)
                }
                else -> Unit
            }
        }.startedLaunch(viewLifecycleOwner)

        viewModel.getCafeList()
    }
}