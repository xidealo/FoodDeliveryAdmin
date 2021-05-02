package com.bunbeauty.fooddeliveryadmin.ui.orders

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.enums.OrderStatus.*
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.domain.resources.IResourcesProvider
import com.bunbeauty.domain.string_helper.IStringHelper
import com.bunbeauty.fooddeliveryadmin.BR
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.BottomSheetChangeStatusBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseBottomSheetDialog
import com.bunbeauty.presentation.view_model.ChangeStatusViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class ChangeStatusBottomSheet :
    BaseBottomSheetDialog<BottomSheetChangeStatusBinding, ChangeStatusViewModel>() {

    override var layoutId = R.layout.bottom_sheet_change_status
    override var viewModelVariable = BR.viewModel

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    @Inject
    lateinit var iStringHelper: IStringHelper

    @Inject
    lateinit var iResourcesProvider: IResourcesProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewDataBinding.order = ChangeStatusBottomSheetArgs.fromBundle(requireArguments()).order
        viewDataBinding.iStringHelper = iStringHelper

        viewDataBinding.dialogChangeStatusBtnCancel.setOnClickListener {
            dismiss()
        }

        viewDataBinding.dialogChangeStatusBtnConfirm.setOnClickListener {
            val currentOrder = ChangeStatusBottomSheetArgs.fromBundle(requireArguments()).order
            val newStatus = when {
                viewDataBinding.dialogChangeStatusRbNotAccepted.isChecked -> NOT_ACCEPTED
                viewDataBinding.dialogChangeStatusRbAccepted.isChecked -> ACCEPTED
                viewDataBinding.dialogChangeStatusRbPreparing.isChecked -> PREPARING
                viewDataBinding.dialogChangeStatusRbSentOut.isChecked -> SENT_OUT
                viewDataBinding.dialogChangeStatusRbDone.isChecked -> DONE
                viewDataBinding.dialogChangeStatusRbCanceled.isChecked -> CANCELED
                else -> NOT_ACCEPTED
            }

            if (newStatus == CANCELED) {
                showCanceledAlert(newStatus, currentOrder)
            } else {
                changeStatus(newStatus, currentOrder)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.skipCollapsed = true
        return dialog
    }

    private fun showCanceledAlert(newStatus: OrderStatus, currentOrder: Order) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(iResourcesProvider.getString(R.string.title_change_status_alert))
            .setMessage(
                iResourcesProvider.getString(R.string.text_change_status_cancel)
            )
            .setPositiveButton(iResourcesProvider.getString(R.string.action_change_status_yes)) { _, _ ->
                changeStatus(
                    newStatus,
                    currentOrder
                )
            }
            .setNegativeButton(iResourcesProvider.getString(R.string.action_change_status_no)) { _, _ ->
            }.show()
    }

    private fun changeStatus(newStatus: OrderStatus, currentOrder: Order) {
        if (newStatus != currentOrder.orderEntity.orderStatus) {
            viewModel.changeStatus(currentOrder, newStatus)
        }
        dismiss()
    }
}