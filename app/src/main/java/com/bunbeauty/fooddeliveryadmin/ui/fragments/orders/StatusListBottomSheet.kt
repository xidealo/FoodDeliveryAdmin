package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.bunbeauty.fooddeliveryadmin.Constants.SELECTED_STATUS_KEY
import com.bunbeauty.fooddeliveryadmin.Constants.STATUS_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetStatusListBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.presentation.order.StatusListViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.StatusItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

class StatusListBottomSheet :
    BaseBottomSheetDialog<BottomSheetStatusListBinding, StatusListViewModel>() {

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.getStatusListComponent().create(this).inject(this)
    }

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
        viewDataBinding.bottomSheetStatusListRvList.adapter = fastAdapter
    }
}