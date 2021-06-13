package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import com.bunbeauty.fooddeliveryadmin.extensions.gone
import com.bunbeauty.fooddeliveryadmin.extensions.strikeOutText
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.enums.OrderStatus.*
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrderDetailsBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.presentation.OrderDetailsViewModel
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class OrderDetailsFragment : BaseFragment<FragmentOrderDetailsBinding, OrderDetailsViewModel>() {

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.getOrderDetailsComponent()
            .create(this)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewDataBinding.dialogChangeStatusTvCode.text = viewModel.codeTitle
        viewDataBinding.dialogChangeStatusTvTimeValue.text = viewModel.time
        viewDataBinding.dialogChangeStatusTvPickupMethodValue.text = viewModel.pickupMethod
        viewDataBinding.dialogChangeStatusTvDeferredTimeValue.text = viewModel.deferredTime
        viewDataBinding.dialogChangeStatusTvAddressValue.text = viewModel.address
        viewDataBinding.dialogChangeStatusTvCommentValue.text = viewModel.comment
        viewDataBinding.dialogChangeStatusTvProductListValue.text = viewModel.productList

        viewDataBinding.dialogChangeStatusTvOrderOldTotalCost.strikeOutText()
        viewDataBinding.dialogChangeStatusTvOrderOldTotalCost.text = viewModel.oldTotalCost
        viewDataBinding.dialogChangeStatusTvOrderNewTotalCost.text = viewModel.newTotalCost

        if (viewModel.deferredTime.isEmpty()) {
            viewDataBinding.dialogChangeStatusTvDeferredTimeValue.gone()
            viewDataBinding.dialogChangeStatusTvDeferredTime.gone()
        }

        if (viewModel.comment.isEmpty()) {
            viewDataBinding.dialogChangeStatusTvCommentValue.gone()
            viewDataBinding.dialogChangeStatusTvComment.gone()
        }

        viewDataBinding.dialogChangeStatusBtnCancel.setOnClickListener {
            router.navigateUp()
        }

        viewDataBinding.dialogChangeStatusBtnConfirm.setOnClickListener {
            val currentOrder = Order()
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

    private fun showCanceledAlert(newStatus: OrderStatus, currentOrder: Order) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_change_status_alert)
            .setMessage(R.string.text_change_status_cancel)
            .setPositiveButton(R.string.action_change_status_yes) { _, _ ->
                changeStatus(
                    newStatus,
                    currentOrder
                )
            }
            .setNegativeButton(R.string.action_change_status_no) { _, _ ->
            }.show()
    }

    private fun changeStatus(newStatus: OrderStatus, currentOrder: Order) {
        if (newStatus != currentOrder.orderEntity.orderStatus) {
            currentOrder.orderEntity.orderStatus = newStatus
            viewModel.changeStatus(currentOrder)
        }
    }
}