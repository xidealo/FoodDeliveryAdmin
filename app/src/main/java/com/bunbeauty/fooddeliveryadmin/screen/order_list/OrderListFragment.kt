package com.bunbeauty.fooddeliveryadmin.screen.order_list

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrdersBinding
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.fooddeliveryadmin.screen.option_list.OptionListBottomSheet
import com.bunbeauty.fooddeliveryadmin.screen.order_list.OrderListFragmentDirections.Companion.toOrdersDetailsFragment
import com.bunbeauty.fooddeliveryadmin.util.addSpaceItemDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderListFragment : BaseFragment<FragmentOrdersBinding>() {

    @Inject
    lateinit var orderAdapter: OrderAdapter

    override val viewModel: OrderListViewModel by viewModels()

    private var cafeListBottomSheetJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        binding.run {
            orderListRv.addSpaceItemDecorator(R.dimen.very_small_margin)
            orderListRv.adapter = orderAdapter.apply {
                onClickListener = { orderItemModel ->
                    viewModel.onOrderClicked(orderItemModel.uuid)
                }
            }
            cafeMcv.setOnClickListener {
                viewModel.onCafeClicked()
            }
            viewModel.orderListState.collectWithLifecycle { orderListState ->
                loadingLpi.isInvisible = !orderListState.isLoading
                cafeTv.text = orderListState.selectedCafe?.address
                orderAdapter.submitList(orderListState.orderList)
                handleEvents(orderListState.eventList)
            }
        }
    }

    private fun handleEvents(eventList: List<OrderListState.Event>) {
        eventList.forEach { event ->
            when (event) {
                is OrderListState.Event.OpenCafeListEvent -> {
                    openCafeList(event.cafeList)
                }
                is OrderListState.Event.OpenOrderDetailsEvent -> {
                    openOrderDetails(event.orderUuid)
                }
            }
        }
        viewModel.consumeEvents(eventList)
    }

    private fun openCafeList(cafeList: List<Option>) {
        val isPossibleToOpen = cafeListBottomSheetJob?.let { job ->
            !job.isActive
        } ?: true
        if (isPossibleToOpen) {
            cafeListBottomSheetJob = lifecycleScope.launch {
                OptionListBottomSheet.show(
                    parentFragmentManager,
                    resources.getString(R.string.title_statistic_select_cafe),
                    cafeList
                )?.let { result ->
                    viewModel.onCafeSelected(result.value)
                }
            }
        }
    }

    private fun openOrderDetails(orderUuid: String) {
        findNavController().navigate(toOrdersDetailsFragment(orderUuid))
    }

}