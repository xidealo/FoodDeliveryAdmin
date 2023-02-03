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
import com.bunbeauty.fooddeliveryadmin.navigation.Navigator
import com.bunbeauty.fooddeliveryadmin.screen.error.ErrorDialog
import com.bunbeauty.fooddeliveryadmin.screen.option_list.Option
import com.bunbeauty.fooddeliveryadmin.screen.option_list.OptionListBottomSheet
import com.bunbeauty.fooddeliveryadmin.screen.order_list.OrderListFragmentDirections.Companion.toLoginFragment
import com.bunbeauty.fooddeliveryadmin.screen.order_list.OrderListFragmentDirections.Companion.toOrdersDetailsFragment
import com.bunbeauty.fooddeliveryadmin.screen.order_list.list.OrderAdapter
import com.bunbeauty.fooddeliveryadmin.util.addSpaceItemDecorator
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
                    openOrderDetails(event.orderUuid)
                }
                OrderListState.Event.OpenLoginEvent -> {
                    findNavController().navigate(toLoginFragment())
                }
                OrderListState.Event.ShowError -> {
                    lifecycleScope.launch {
                        ErrorDialog.show(childFragmentManager).let {
                            viewModel.onRetryClicked()
                        }
                    }
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

    private fun openLogoutConfirmation() {
        lifecycleScope.launch {
            OptionListBottomSheet.show(
                fragmentManager = parentFragmentManager,
                title = resources.getString(R.string.title_logout),
                options = listOf(
                    Option(
                        id = LogoutOption.LOGOUT.name,
                        title = resources.getString(R.string.action_logout),
                        isPrimary = true
                    ),
                    Option(
                        id = LogoutOption.CANCEL.name,
                        title = resources.getString(R.string.action_cancel)
                    )
                ),
                isCenter = true
            )?.value?.let { resultValue ->
                viewModel.onLogout(resultValue)
            }
        }
    }

    private fun openOrderDetails(orderUuid: String) {
        findNavController().navigate(toOrdersDetailsFragment(orderUuid))
    }
}
