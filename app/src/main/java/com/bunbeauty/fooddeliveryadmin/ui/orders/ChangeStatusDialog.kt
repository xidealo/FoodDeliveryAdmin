package com.bunbeauty.fooddeliveryadmin.ui.orders

import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.DialogChangeStatusBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseDialog
import com.bunbeauty.fooddeliveryadmin.view_model.ChangeStatusViewModel
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.enums.OrderStatus
import java.lang.ref.WeakReference


class ChangeStatusDialog : BaseDialog<DialogChangeStatusBinding, ChangeStatusViewModel>(),
    ChangeStatusNavigator {

    override var dataBindingVariable: Int = BR.viewModel
    override var layoutId: Int = R.layout.dialog_change_status
    override var viewModelClass = ChangeStatusViewModel::class.java

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = WeakReference(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewDataBinding.order = ChangeStatusDialogArgs.fromBundle(requireArguments()).order
        super.onViewCreated(view, savedInstanceState)
    }

    override fun closeDialog() {
        dismiss()
    }

    override fun updateClick() {
        val currentOrder = ChangeStatusDialogArgs.fromBundle(requireArguments()).order

        if (viewDataBinding.dialogChangeStatusRbNotAccepted.isChecked && currentOrder.orderStatus == OrderStatus.NotAccepted) {
            dismiss()
            return
        }
        if (viewDataBinding.dialogChangeStatusRbPreparing.isChecked && currentOrder.orderStatus == OrderStatus.Preparing) {
            dismiss()
            return
        }
        if (viewDataBinding.dialogChangeStatusRbSentOut.isChecked && currentOrder.orderStatus == OrderStatus.SentOut) {
            dismiss()
            return
        }
        if (viewDataBinding.dialogChangeStatusRbDelivered.isChecked && currentOrder.orderStatus == OrderStatus.Delivered) {
            dismiss()
            return
        }

        if (viewDataBinding.dialogChangeStatusRbNotAccepted.isChecked)
            currentOrder.orderStatus = OrderStatus.NotAccepted

        if (viewDataBinding.dialogChangeStatusRbPreparing.isChecked)
            currentOrder.orderStatus = OrderStatus.Preparing

        if (viewDataBinding.dialogChangeStatusRbSentOut.isChecked)
            currentOrder.orderStatus = OrderStatus.SentOut

        if (viewDataBinding.dialogChangeStatusRbDelivered.isChecked)
            currentOrder.orderStatus = OrderStatus.Delivered

        viewModel.changeStatus(currentOrder)
    }
}