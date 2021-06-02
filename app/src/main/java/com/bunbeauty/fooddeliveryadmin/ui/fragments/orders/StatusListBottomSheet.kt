package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.bunbeauty.common.Constants.SELECTED_STATUS_KEY
import com.bunbeauty.common.Constants.STATUS_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetStatusListBinding
import com.bunbeauty.fooddeliveryadmin.presentation.order.StatusListViewModel
import com.bunbeauty.fooddeliveryadmin.ui.items.StatusItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatusListBottomSheet : BaseBottomSheetDialog<BottomSheetStatusListBinding>() {

    override val viewModel: StatusListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemAdapter = ItemAdapter<StatusItem>().apply {
            set(viewModel.statusList)
        }
        val fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.onClickListener = { _, _, statusItem, _ ->
            setFragmentResult(STATUS_REQUEST_KEY, bundleOf(SELECTED_STATUS_KEY to statusItem))
            viewModel.goBack()
            false
        }
        binding.bottomSheetStatusListRvList.adapter = fastAdapter
    }
}