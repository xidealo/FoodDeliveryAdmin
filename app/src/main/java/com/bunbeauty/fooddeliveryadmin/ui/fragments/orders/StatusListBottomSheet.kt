package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetStatusListBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.presentation.EmptyViewModel
import com.bunbeauty.fooddeliveryadmin.presentation.order.StatusListViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.AddressItem
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
        viewDataBinding.bottomSheetStatusListRvList.adapter = fastAdapter
    }
}