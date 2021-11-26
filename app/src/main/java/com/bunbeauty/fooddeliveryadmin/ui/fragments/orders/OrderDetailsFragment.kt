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
import com.bunbeauty.fooddeliveryadmin.extensions.startedLaunch
import com.bunbeauty.fooddeliveryadmin.extensions.strikeOutText
import com.bunbeauty.fooddeliveryadmin.ui.base.BaseFragment
import com.bunbeauty.fooddeliveryadmin.ui.fragments.orders.OrdersFragmentDirections.*
import com.bunbeauty.fooddeliveryadmin.ui.items.CartProductItem
import com.bunbeauty.presentation.model.list.OrderStatus
import com.bunbeauty.presentation.navigation_event.OrderDetailsNavigationEvent
import com.bunbeauty.presentation.view_model.order.OrderDetailsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OrderDetailsFragment : BaseFragment<FragmentOrderDetailsBinding>() {

    override val viewModel: OrderDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            fragmentOrderDetailsBtnBack.setOnClickListener {
                viewModel.goBack()
            }
            fragmentOrderDetailsTvCode.text = viewModel.codeTitle
            fragmentOrderDetailsTvPhoneValue.text = viewModel.phone
            fragmentOrderDetailsTvTimeValue.text = viewModel.time
            fragmentOrderDetailsTvPickupMethodValue.text = viewModel.pickupMethod
            fragmentOrderDetailsTvDeferredTimeValue.text = viewModel.deferredTime
            fragmentOrderDetailsTvAddressValue.text = viewModel.address
            fragmentOrderDetailsTvCommentValue.text = viewModel.comment
            fragmentOrderDetailsCvStatus.cardText = viewModel.status
            fragmentOrderDetailsCvStatus.setOnClickListener {
                viewModel.goToStatusList()
            }
            setFragmentResultListener(STATUS_REQUEST_KEY) { _, bundle ->
                bundle.getParcelable<OrderStatus>(SELECTED_STATUS_KEY)?.let { orderStatus ->
                    viewModel.status = orderStatus.title
                    fragmentOrderDetailsCvStatus.cardText = orderStatus.title
                }
            }
            val itemAdapter = ItemAdapter<CartProductItem>()
            val items = viewModel.productList.map { cartProductItemModel ->
                CartProductItem(cartProductItemModel)
            }
            itemAdapter.set(items)
            val fastAdapter = FastAdapter.with(itemAdapter)
            fragmentOrderDetailsRvProductList.adapter = fastAdapter
            fragmentOrderDetailsTvDeliveryCostValue.text = viewModel.deliveryCost
            fragmentOrderDetailsTvBonusesValue.text = viewModel.bonuses
            fragmentOrderDetailsTvOrderOldTotalCost.strikeOutText()
            fragmentOrderDetailsTvOrderOldTotalCost.text = viewModel.oldOrderCost
            fragmentOrderDetailsTvOrderNewTotalCost.text = viewModel.newOrderCost
            if (viewModel.deferredTime.isNullOrEmpty()) {
                fragmentOrderDetailsTvDeferredTimeValue.gone()
                fragmentOrderDetailsTvDeferredTime.gone()
            }
            if (viewModel.comment.isEmpty()) {
                fragmentOrderDetailsTvCommentValue.gone()
                fragmentOrderDetailsTvComment.gone()
            }
            if (!viewModel.isDelivery) {
                fragmentOrderDetailsTvDeliveryCostValue.gone()
                fragmentOrderDetailsTvDeliveryCost.gone()
            }
            if (viewModel.bonuses.isEmpty()) {
                fragmentOrderDetailsTvBonusesValue.gone()
                fragmentOrderDetailsTvBonuses.gone()
            }
            fragmentOrderDetailsBtnCancel.setOnClickListener {
                viewModel.goBack()
            }
            fragmentOrderDetailsBtnSave.setOnClickListener {
                val status = fragmentOrderDetailsCvStatus.cardText
                if (viewModel.isStatusCanceled(status)) {
                    showCanceledAlert(status)
                } else {
                    viewModel.changeStatus(status)
                }
            }
        }
        viewModel.navigation.onEach { navigationEvent ->
            when (navigationEvent) {
                is OrderDetailsNavigationEvent.ToStatusList ->
                    router.navigate(OrderDetailsFragmentDirections.toListBottomSheet(navigationEvent.listData))
                else -> Unit
            }
        }.startedLaunch(viewLifecycleOwner)
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