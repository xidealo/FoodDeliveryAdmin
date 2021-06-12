package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bunbeauty.common.Constants.SELECTED_STATUS_KEY
import com.bunbeauty.common.Constants.STATUS_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrderDetailsBinding
import com.bunbeauty.fooddeliveryadmin.extensions.gone
import com.bunbeauty.fooddeliveryadmin.extensions.strikeOutText
import com.bunbeauty.fooddeliveryadmin.presentation.order.OrderDetailsViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.CartProductItem
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.StatusItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsFragment : BaseFragment<FragmentOrderDetailsBinding>() {

    override val viewModel: OrderDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.fragmentOrderDetailsBtnBack.setOnClickListener {
            viewModel.goBack()
        }
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
        setFragmentResultListener(STATUS_REQUEST_KEY) { _, bundle ->
            bundle.getParcelable<StatusItem>(SELECTED_STATUS_KEY)?.let { statusItem ->
                viewModel.status = statusItem.status
                viewDataBinding.fragmentOrderDetailsCvStatus.cardText = statusItem.status
            }
        }
        val itemAdapter = ItemAdapter<CartProductItem>().apply {
            set(viewModel.productList)
        }
        val fastAdapter = FastAdapter.with(itemAdapter)
        viewDataBinding.fragmentOrderDetailsRvProductList.adapter = fastAdapter
        viewDataBinding.fragmentOrderDetailsTvDeliveryCostValue.text = viewModel.deliveryCost
        viewDataBinding.fragmentOrderDetailsTvOrderOldTotalCost.strikeOutText()
        viewDataBinding.fragmentOrderDetailsTvOrderOldTotalCost.text = viewModel.oldOrderCost
        viewDataBinding.fragmentOrderDetailsTvOrderNewTotalCost.text = viewModel.newOrderCost
        if (viewModel.deferredTime.isNullOrEmpty()) {
            viewDataBinding.fragmentOrderDetailsTvDeferredTimeValue.gone()
            viewDataBinding.fragmentOrderDetailsTvDeferredTime.gone()
        }
        if (viewModel.comment.isEmpty()) {
            viewDataBinding.fragmentOrderDetailsTvCommentValue.gone()
            viewDataBinding.fragmentOrderDetailsTvComment.gone()
        }
        if (!viewModel.isDelivery) {
            viewDataBinding.fragmentOrderDetailsTvDeliveryCostValue.gone()
            viewDataBinding.fragmentOrderDetailsTvDeliveryCost.gone()
        }
        viewDataBinding.fragmentOrderDetailsBtnCancel.setOnClickListener {
            viewModel.goBack()
        }
        viewDataBinding.fragmentOrderDetailsBtnSave.setOnClickListener {
            val status = viewDataBinding.fragmentOrderDetailsCvStatus.cardText
            if (viewModel.isStatusCanceled(status)) {
                showCanceledAlert(status)
            } else {
                viewModel.changeStatus(status)
            }
        }
    }

    private fun showCanceledAlert(status: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_order_details_alert)
            .setMessage(R.string.msg_order_details_alert)
            .setPositiveButton(R.string.action_order_details_yes) { _, _ ->
                viewModel.changeStatus(status)
            }
            .setNegativeButton(R.string.action_order_details_no) { _, _ -> }
            .show()
    }
}