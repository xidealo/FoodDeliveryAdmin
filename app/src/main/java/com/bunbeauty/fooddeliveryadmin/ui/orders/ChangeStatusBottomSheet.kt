package com.bunbeauty.fooddeliveryadmin.ui.orders

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetChangeStatusBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.data.enums.OrderStatus.*
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.bunbeauty.fooddeliveryadmin.ui.orders.ChangeStatusBottomSheetArgs.fromBundle
import com.bunbeauty.domain.string_helper.IStringHelper
import com.bunbeauty.fooddeliveryadmin.view_model.ChangeStatusViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.ref.WeakReference
import javax.inject.Inject


class ChangeStatusBottomSheet :
    BaseBottomSheetDialog<BottomSheetChangeStatusBinding, ChangeStatusViewModel>(),
    ChangeStatusNavigator {

    override var layoutId = R.layout.bottom_sheet_change_status
    override var viewModelVariable = BR.viewModel

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var iStringHelper: IStringHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewDataBinding.order = fromBundle(requireArguments()).order
        viewDataBinding.iStringHelper = iStringHelper
        viewModel.navigator = WeakReference(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true
        return dialog
    }

    override fun closeDialog() {
        dismiss()
    }

    override fun updateClick() {
        val currentOrder = fromBundle(requireArguments()).order

        val newStatus = when {
            viewDataBinding.dialogChangeStatusRbNotAccepted.isChecked -> NOT_ACCEPTED
            viewDataBinding.dialogChangeStatusRbPreparing.isChecked -> PREPARING
            viewDataBinding.dialogChangeStatusRbSentOut.isChecked -> SENT_OUT
            viewDataBinding.dialogChangeStatusRbDelivered.isChecked -> DONE
            else -> NOT_ACCEPTED
        }

        if (newStatus != currentOrder.orderEntity.orderStatus) {
            viewModel.changeStatus(currentOrder, newStatus)
        }
        dismiss()
    }
}