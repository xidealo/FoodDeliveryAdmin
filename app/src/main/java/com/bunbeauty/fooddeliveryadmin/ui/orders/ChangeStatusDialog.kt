package com.bunbeauty.fooddeliveryadmin.ui.orders

import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.DialogChangeStatusBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseDialog
import com.bunbeauty.fooddeliveryadmin.view_model.ChangeStatusViewModel
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.enums.OrderStatus.*
import com.bunbeauty.fooddeliveryadmin.ui.orders.ChangeStatusDialogArgs.fromBundle
import com.bunbeauty.fooddeliveryadmin.utils.string.IStringHelper
import java.lang.ref.WeakReference
import javax.inject.Inject

class ChangeStatusDialog : BaseDialog<DialogChangeStatusBinding, ChangeStatusViewModel>(),
    ChangeStatusNavigator {

    override var dataBindingVariable: Int = BR.viewModel
    override var layoutId: Int = R.layout.dialog_change_status
    override var viewModelClass = ChangeStatusViewModel::class.java

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var iStringHelper: IStringHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = WeakReference(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewDataBinding.order = fromBundle(requireArguments()).order
        viewDataBinding.iStringHelper = iStringHelper
        super.onViewCreated(view, savedInstanceState)
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