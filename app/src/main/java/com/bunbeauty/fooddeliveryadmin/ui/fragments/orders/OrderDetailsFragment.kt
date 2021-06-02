
package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.bunbeauty.common.Constants.SELECTED_STATUS_KEY
import com.bunbeauty.common.Constants.STATUS_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrderDetailsBinding
import com.bunbeauty.fooddeliveryadmin.extensions.gone
import com.bunbeauty.fooddeliveryadmin.extensions.strikeOutText
import com.bunbeauty.fooddeliveryadmin.presentation.order.OrderDetailsViewModel
import com.bunbeauty.fooddeliveryadmin.ui.items.CartProductItem
import com.bunbeauty.fooddeliveryadmin.ui.items.StatusItem
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

        binding.fragmentOrderDetailsBtnBack.setOnClickListener {
            viewModel.goBack()
        }
        binding.fragmentOrderDetailsTvCode.text = viewModel.codeTitle
        binding.fragmentOrderDetailsTvPhoneValue.text = viewModel.phone
        binding.fragmentOrderDetailsTvTimeValue.text = viewModel.time
        binding.fragmentOrderDetailsTvPickupMethodValue.text = viewModel.pickupMethod
        binding.fragmentOrderDetailsTvDeferredTimeValue.text = viewModel.deferredTime
        binding.fragmentOrderDetailsTvAddressValue.text = viewModel.address
        binding.fragmentOrderDetailsTvCommentValue.text = viewModel.comment
        binding.fragmentOrderDetailsCvStatus.cardText = viewModel.status
        binding.fragmentOrderDetailsCvStatus.setOnClickListener {
            viewModel.goToStatusList()
        }
        setFragmentResultListener(STATUS_REQUEST_KEY) { _, bundle ->
            bundle.getParcelable<StatusItem>(SELECTED_STATUS_KEY)?.let { statusItem ->
                viewModel.status = statusItem.status
                binding.fragmentOrderDetailsCvStatus.cardText = statusItem.status
            }
        }
        val itemAdapter = ItemAdapter<CartProductItem>().apply {
            set(viewModel.productList)
        }
        val fastAdapter = FastAdapter.with(itemAdapter)
        binding.fragmentOrderDetailsRvProductList.adapter = fastAdapter
        binding.fragmentOrderDetailsTvDeliveryCostValue.text = viewModel.deliveryCost
        binding.fragmentOrderDetailsTvBonusesValue.text = viewModel.bonuses
        binding.fragmentOrderDetailsTvOrderOldTotalCost.strikeOutText()
        binding.fragmentOrderDetailsTvOrderOldTotalCost.text = viewModel.oldOrderCost
        binding.fragmentOrderDetailsTvOrderNewTotalCost.text = viewModel.newOrderCost
        if (viewModel.deferredTime.isNullOrEmpty()) {
            binding.fragmentOrderDetailsTvDeferredTimeValue.gone()
            binding.fragmentOrderDetailsTvDeferredTime.gone()
        }
        if (viewModel.comment.isEmpty()) {
            binding.fragmentOrderDetailsTvCommentValue.gone()
            binding.fragmentOrderDetailsTvComment.gone()
        }
        if (!viewModel.isDelivery) {
            binding.fragmentOrderDetailsTvDeliveryCostValue.gone()
            binding.fragmentOrderDetailsTvDeliveryCost.gone()
        }
        if (viewModel.bonuses.isEmpty()) {
            binding.fragmentOrderDetailsTvBonusesValue.gone()
            binding.fragmentOrderDetailsTvBonuses.gone()
        }
        binding.fragmentOrderDetailsBtnCancel.setOnClickListener {
            viewModel.goBack()
        }
        binding.fragmentOrderDetailsBtnSave.setOnClickListener {
            val status = binding.fragmentOrderDetailsCvStatus.cardText
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