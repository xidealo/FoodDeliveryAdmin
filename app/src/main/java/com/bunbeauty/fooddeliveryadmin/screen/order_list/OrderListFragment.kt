package com.bunbeauty.fooddeliveryadmin.screen.order_list

import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.FragmentOrdersBinding
import com.bunbeauty.fooddeliveryadmin.navigation.Navigator
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.fooddeliveryadmin.notification.LAST_ORDER_NOTIFICATION_ID
import com.bunbeauty.fooddeliveryadmin.screen.error.ErrorDialog
import com.bunbeauty.fooddeliveryadmin.screen.option_list.OptionListBottomSheet
import com.bunbeauty.fooddeliveryadmin.screen.order_list.OrderListFragmentDirections.Companion.toLoginFragment
import com.bunbeauty.fooddeliveryadmin.screen.order_list.OrderListFragmentDirections.Companion.toOrdersDetailsFragment
import com.bunbeauty.fooddeliveryadmin.screen.order_list.list.OrderAdapter
import com.bunbeauty.fooddeliveryadmin.util.addSpaceItemDecorator
import com.bunbeauty.presentation.Option
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderListFragment : BaseFragment<FragmentOrdersBinding>() {

    @Inject
    lateinit var orderAdapter: OrderAdapter

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var notificationManagerCompat: NotificationManagerCompat

    override val viewModel: OrderListViewModel by viewModels()

    private var cafeListBottomSheetJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        binding.run {
            orderListRv.addSpaceItemDecorator(R.dimen.very_small_margin)
            orderListRv.adapter = orderAdapter.apply {
                onClickListener = { orderItemModel ->
                    viewModel.onOrderClicked(orderItemModel)
                }
            }
            cafeMcv.setOnClickListener {
                viewModel.onCafeClicked()
            }
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.logout -> {
                        lifecycleScope.launch {
                            navigator.openLogout(parentFragmentManager)?.let { option ->
                                viewModel.onLogout(option)
                            }
                        }
                        true
                    }
                    else -> {
                        false
                    }
                }
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
                is OrderListState.Event.ScrollToTop -> {
                    lifecycleScope.launch {
                        delay(500)
                        binding.orderListRv.smoothScrollToPosition(0)
                    }
                }
                is OrderListState.Event.OpenCafeListEvent -> {
                    openCafeList(event.cafeList)
                }
                is OrderListState.Event.OpenOrderDetailsEvent -> {
                    openOrderDetails(event.orderUuid, event.orderCode)
                }
                OrderListState.Event.OpenLoginEvent -> {
                    findNavController().navigateSafe(toLoginFragment())
                }
                OrderListState.Event.ShowError -> {
                    lifecycleScope.launch {
                        ErrorDialog.show(childFragmentManager).let {
                            viewModel.onRetryClicked()
                        }
                    }
                }
                OrderListState.Event.CancelNotification -> {
                    notificationManagerCompat.cancel(LAST_ORDER_NOTIFICATION_ID)
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
                    resources.getString(R.string.title_orders_select_cafe),
                    cafeList
                )?.let { result ->
                    viewModel.onCafeSelected(result.value)
                }
            }
        }
    }

    private fun openOrderDetails(orderUuid: String, orderCode: String) {
        findNavController().navigateSafe(toOrdersDetailsFragment(orderUuid, orderCode))
    }
}
