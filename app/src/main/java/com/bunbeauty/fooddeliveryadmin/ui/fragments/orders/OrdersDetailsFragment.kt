package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import com.bunbeauty.common.extensions.gone
import com.bunbeauty.common.extensions.strikeOutText
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.enums.OrderStatus.*
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrderDetailsBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ViewModelComponent
import com.bunbeauty.fooddeliveryadmin.presentation.OrderDetailsViewModel
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrdersDetailsFragmentArgs.fromBundle
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class OrdersDetailsFragment :
    BaseFragment<FragmentOrderDetailsBinding, OrderDetailsViewModel>() {

    override fun inject(viewModelComponent: ViewModelComponent) {
        viewModelComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val orderUI = fromBundle(requireArguments()).orderUI

        viewDataBinding.dialogChangeStatusTvCode.text = viewModel.getCodeTitle(orderUI.code)
        viewDataBinding.dialogChangeStatusTvTimeValue.text = orderUI.time
        viewDataBinding.dialogChangeStatusTvPickupMethodValue.text =
            viewModel.getPickupMethod(orderUI.isDelivery)
        viewDataBinding.dialogChangeStatusTvDeferredTimeValue.text = orderUI.deferredTime
        viewDataBinding.dialogChangeStatusTvAddressValue.text = orderUI.address
        viewDataBinding.dialogChangeStatusTvCommentValue.text = orderUI.comment
        viewDataBinding.dialogChangeStatusTvProductListValue.text =
            viewModel.getProductsList(orderUI.cartProductList)

        viewDataBinding.dialogChangeStatusTvOrderOldTotalCost.strikeOutText()
        viewDataBinding.dialogChangeStatusTvOrderOldTotalCost.text = orderUI.oldTotalCost
        viewDataBinding.dialogChangeStatusTvOrderNewTotalCost.text = orderUI.newTotalCost

        if (orderUI.deferredTime.isEmpty()) {
            viewDataBinding.dialogChangeStatusTvDeferredTimeValue.gone()
            viewDataBinding.dialogChangeStatusTvDeferredTime.gone()
        }

        if (orderUI.comment.isEmpty()) {
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