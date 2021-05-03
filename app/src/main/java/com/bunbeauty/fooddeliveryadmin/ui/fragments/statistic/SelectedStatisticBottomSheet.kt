package com.bunbeauty.fooddeliveryadmin.ui.fragments.statistic

import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetSelectedStatisticBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.bunbeauty.domain.string_helper.IStringHelper
import com.bunbeauty.fooddeliveryadmin.presentation.SelectedStatisticViewModel
import javax.inject.Inject

class SelectedStatisticBottomSheet :
    BaseBottomSheetDialog<BottomSheetSelectedStatisticBinding, SelectedStatisticViewModel>() {

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var stringHelper: IStringHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.statistic =
            SelectedStatisticBottomSheetArgs.fromBundle(requireArguments()).statistic
        viewDataBinding.stringHelper = stringHelper
    }
}