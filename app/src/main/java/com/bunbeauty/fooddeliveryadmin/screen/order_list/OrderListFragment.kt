package com.bunbeauty.fooddeliveryadmin.screen.order_list

import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrdersBinding
import com.bunbeauty.fooddeliveryadmin.screen.option_list.OptionListBottomSheet
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
                    // TODO open order
                }
            }
            cafeMcv.setOnClickListener {
                viewModel.onCafeClicked()
            }
            viewModel.orderListState.collectWithLifecycle { orderListState ->
                loadingLpi.isInvisible = !orderListState.isLoading
                cafeTv.text = orderListState.selectedCafe?.title
                orderAdapter.submitList(orderListState.orderList)
                handleEvents(orderListState.eventList)
            }
        }
    }

    private fun handleEvents(eventList: List<OrderListState.Event>) {
        eventList.forEach { event ->
            when (event) {
                is OrderListState.Event.OpenCafeListEvent -> {
                    val isPossibleToOpen = cafeListBottomSheetJob?.let { job ->
                        !job.isActive
                    } ?: true
                    if (isPossibleToOpen) {
                        cafeListBottomSheetJob = lifecycleScope.launch {
                            OptionListBottomSheet.show(
                                parentFragmentManager,
                                resources.getString(R.string.title_statistic_select_cafe),
                                event.cafeList
                            )?.let { result ->
                                viewModel.onCafeSelected(result.value)
                            }
                        }
                    }
                }
            }
        }
        viewModel.consumeEvents(eventList)
    }
}