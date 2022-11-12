package com.bunbeauty.fooddeliveryadmin.screen.order_details

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.core_ui.FIELD_ITEM_CALLBACK
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrderDetailsBinding
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.fooddeliveryadmin.screen.option_list.OptionListBottomSheet
import com.bunbeauty.fooddeliveryadmin.screen.order_details.item.getOrderDetailsDelegate
import com.bunbeauty.fooddeliveryadmin.screen.order_details.item.getOrderProductDelegate
import com.bunbeauty.fooddeliveryadmin.util.startedLaunch
import com.bunbeauty.fooddeliveryadmin.util.strikeOutText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderDetailsFragment : BaseFragment<FragmentOrderDetailsBinding>() {

    override val viewModel: OrderDetailsViewModel by viewModels()

    private var statusListJob: Job? = null

    private val adapter = AsyncListDifferDelegationAdapter(
        FIELD_ITEM_CALLBACK,
        getOrderDetailsDelegate { viewModel.onStatusClicked() },
        getOrderProductDelegate()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.orderDetailsState.onEach { orderDetailsState ->
            binding.apply {
                codeTv.text = orderDetailsState.order?.code

                adapter.items = orderDetailsState.itemModelList

                val isDelivery = orderDetailsState.order?.delivery ?: false
                deliveryCostTv.isVisible = isDelivery
                deliveryCostValueTv.isVisible = isDelivery
                deliveryCostValueTv.text = orderDetailsState.deliveryCost

                discountTv.isVisible = orderDetailsState.discount != null
                discountValueTv.isVisible = orderDetailsState.discount != null
                discountValueTv.text = orderDetailsState.discount

                finalCostOldValueTv.isVisible = orderDetailsState.oldFinalCost != null
                finalCostOldValueTv.text = orderDetailsState.oldFinalCost
                finalCostOldValueTv.strikeOutText()
                finalCostNewValueTv.text = orderDetailsState.newFinalCost
            }

            handleEventList(orderDetailsState.eventList)
        }.startedLaunch(viewLifecycleOwner)

        binding.apply {
            toolbar.setNavigationOnClickListener {
                goBack()
            }
            detailsRv.adapter = adapter
            cancelBtn.setOnClickListener {
                goBack()
            }
            saveBtn.setOnClickListener {
                viewModel.onSaveClicked()
            }
        }
    }

    private fun handleEventList(eventList: List<OrderDetailsState.Event>) {
        eventList.forEach { event ->
            when (event) {
                is OrderDetailsState.OpenStatusListEvent -> {
                    openStatusList(event.statusList)
                }
                OrderDetailsState.OpenWarningDialogEvent -> {
                    showCancellationWarning()
                }
                OrderDetailsState.GoBackEvent -> {
                    goBack()
                }
            }
        }
        viewModel.consumeEvents(eventList)
    }

    private fun openStatusList(statusList: List<Option>) {
        if (statusListJob?.isActive == true) {
            return
        }
        statusListJob = lifecycleScope.launch {
            OptionListBottomSheet.show(
                parentFragmentManager,
                resources.getString(R.string.title_order_status),
                statusList
            )?.value?.let { resultValue ->
                viewModel.onStatusSelected(resultValue)
            }
        }
    }

    private fun showCancellationWarning() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_order_details_alert)
            .setMessage(R.string.msg_order_details_alert)
            .setPositiveButton(R.string.action_order_details_yes) { _, _ ->
                viewModel.onCancellationConfirmed()
            }
            .setNegativeButton(R.string.action_order_details_no) { _, _ -> }
            .show()
    }

    private fun goBack() {
        findNavController().navigateUp()
    }
}