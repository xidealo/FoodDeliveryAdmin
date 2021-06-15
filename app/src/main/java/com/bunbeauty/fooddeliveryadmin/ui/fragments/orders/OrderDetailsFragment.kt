package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.enums.OrderStatus.*
import com.bunbeauty.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrderDetailsBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.extensions.gone
import com.bunbeauty.fooddeliveryadmin.extensions.strikeOutText
import com.bunbeauty.fooddeliveryadmin.presentation.order.OrderDetailsViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.CartProductItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.utils.DefaultItemList

class OrderDetailsFragment : BaseFragment<FragmentOrderDetailsBinding, OrderDetailsViewModel>() {

    override fun inject(activityComponent: ActivityComponent) {
        activityComponent.getOrderDetailsComponent()
            .create(this)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.fragmentOrderDetailsTvCode.text = viewModel.codeTitle
        viewDataBinding.fragmentOrderDetailsTvTimeValue.text = viewModel.time
        viewDataBinding.fragmentOrderDetailsTvPickupMethodValue.text = viewModel.pickupMethod
        viewDataBinding.fragmentOrderDetailsTvDeferredTimeValue.text = viewModel.deferredTime
        viewDataBinding.fragmentOrderDetailsTvAddressValue.text = viewModel.address
        viewDataBinding.fragmentOrderDetailsTvCommentValue.text = viewModel.comment
        viewDataBinding.fragmentOrderDetailsCvStatus.cardText = viewModel.status
        viewDataBinding.fragmentOrderDetailsCvStatus.setOnClickListener {
            viewModel.goToStatusList()
        }
        val itemAdapter = ItemAdapter<CartProductItem>().apply {
            set(viewModel.productList)
        }
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.fragmentOrderDetailsRvProductList.adapter = fastAdapter

        viewDataBinding.fragmentOrderDetailsTvOrderOldTotalCost.strikeOutText()
        viewDataBinding.fragmentOrderDetailsTvOrderOldTotalCost.text = viewModel.oldTotalCost
        viewDataBinding.fragmentOrderDetailsTvOrderNewTotalCost.text = viewModel.newTotalCost

        if (viewModel.deferredTime.isEmpty()) {
            viewDataBinding.fragmentOrderDetailsTvDeferredTimeValue.gone()
            viewDataBinding.fragmentOrderDetailsTvDeferredTime.gone()
        }

        if (viewModel.comment.isEmpty()) {
            viewDataBinding.fragmentOrderDetailsTvCommentValue.gone()
            viewDataBinding.fragmentOrderDetailsTvComment.gone()
        }

        viewDataBinding.fragmentOrderDetailsBtnCancel.setOnClickListener {
            router.navigateUp()
        }

        viewDataBinding.fragmentOrderDetailsBtnConfirm.setOnClickListener {
            val currentOrder = Order()
            val newStatus = when {
                /*viewDataBinding.fragmentOrderDetailsRbNotAccepted.isChecked -> NOT_ACCEPTED
                viewDataBinding.fragmentOrderDetailsRbAccepted.isChecked -> ACCEPTED
                viewDataBinding.fragmentOrderDetailsRbPreparing.isChecked -> PREPARING
                viewDataBinding.fragmentOrderDetailsRbSentOut.isChecked -> SENT_OUT
                viewDataBinding.fragmentOrderDetailsRbDone.isChecked -> DONE
                viewDataBinding.fragmentOrderDetailsRbCanceled.isChecked -> CANCELED*/
                else -> NOT_ACCEPTED
            }

            if (newStatus == CANCELED) {
                showCanceledAlert(newStatus, currentOrder)
            } else {
                changeStatus(newStatus, currentOrder)
            }
        }
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