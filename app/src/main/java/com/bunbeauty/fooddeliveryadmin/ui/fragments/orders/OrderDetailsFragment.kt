package com.bunbeauty.fooddeliveryadmin.ui.fragments.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import com.bunbeauty.fooddeliveryadmin.Constants.SELECTED_STATUS_KEY
import com.bunbeauty.fooddeliveryadmin.Constants.STATUS_REQUEST_KEY
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrderDetailsBinding
import com.bunbeauty.fooddeliveryadmin.di.components.ActivityComponent
import com.bunbeauty.fooddeliveryadmin.extensions.gone
import com.bunbeauty.fooddeliveryadmin.extensions.strikeOutText
import com.bunbeauty.fooddeliveryadmin.presentation.order.OrderDetailsViewModel
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.CartProductItem
import com.bunbeauty.fooddeliveryadmin.ui.adapter.items.StatusItem
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

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
        viewDataBinding.fragmentOrderDetailsTvOrderOldTotalCost.strikeOutText()
        viewDataBinding.fragmentOrderDetailsTvOrderOldTotalCost.text = viewModel.oldTotalCost
        viewDataBinding.fragmentOrderDetailsTvOrderNewTotalCost.text = viewModel.newTotalCost
        if (viewModel.deferredTime.isNullOrEmpty()) {
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
            .setTitle(R.string.title_change_status_alert)
            .setMessage(R.string.text_change_status_cancel)
            .setPositiveButton(R.string.action_change_status_yes) { _, _ ->
                viewModel.changeStatus(status)
            }
            .setNegativeButton(R.string.action_change_status_no) { _, _ -> }
            .show()
    }
}