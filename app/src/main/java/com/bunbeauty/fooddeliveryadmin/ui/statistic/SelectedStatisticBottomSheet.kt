package com.bunbeauty.fooddeliveryadmin.ui.statistic

import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetSelectedStatisticBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.bunbeauty.fooddeliveryadmin.utils.string.IStringHelper
import com.bunbeauty.fooddeliveryadmin.view_model.SelectedStatisticViewModel
import javax.inject.Inject

class SelectedStatisticBottomSheet :
    BaseBottomSheetDialog<BottomSheetSelectedStatisticBinding, SelectedStatisticViewModel>() {

    override var layoutId = R.layout.bottom_sheet_selected_statistic
    override var viewModelVariable = BR.viewModel

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var iStringHelper: IStringHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.statistic =
            SelectedStatisticBottomSheetArgs.fromBundle(requireArguments()).statistic
        viewDataBinding.iStringHelper = iStringHelper
    }
}