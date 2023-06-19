package com.bunbeauty.fooddeliveryadmin.screen.order_list

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationTextCard
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.setContentWithTheme
import com.bunbeauty.fooddeliveryadmin.core_ui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
import com.bunbeauty.fooddeliveryadmin.navigation.Navigator
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.fooddeliveryadmin.notification.LAST_ORDER_NOTIFICATION_ID
import com.bunbeauty.fooddeliveryadmin.screen.error.ErrorDialog
import com.bunbeauty.fooddeliveryadmin.screen.option_list.OptionListBottomSheet
import com.bunbeauty.fooddeliveryadmin.screen.order_list.OrderListFragmentDirections.Companion.toLoginFragment
import com.bunbeauty.fooddeliveryadmin.screen.order_list.OrderListFragmentDirections.Companion.toOrdersDetailsFragment
import com.bunbeauty.fooddeliveryadmin.screen.order_list.list.OrderAdapter
import com.bunbeauty.presentation.Option
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderListFragment : BaseFragment<LayoutComposeBinding>() {

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

        binding.root.setContentWithTheme {
            val uiState by viewModel.orderListUiState.collectAsStateWithLifecycle()
            OrderListScreen(
                uiState = uiState.state,
                onCafeClicked = viewModel::onCafeClicked,
                onOrderClicked = viewModel::onOrderClicked,
            )
            LaunchedEffect(uiState.eventList) {
                handleEventList(uiState.eventList)
            }
        }
    }

    @Composable
    private fun OrderListScreen(
        uiState: OrderListUiState.State,
        onCafeClicked: () -> Unit,
        onOrderClicked: (OrderListUiState.OrderItem) -> Unit,
    ) {
        AdminScaffold(title = stringResource(R.string.title_orders)) {
            when (uiState) {
                OrderListUiState.State.Loading -> {
                    LoadingScreen()
                }
                OrderListUiState.State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {}
                    )
                }
                is OrderListUiState.State.Success -> {
                    SuccessOrderListScreen(
                        uiStateSuccess = uiState,
                        onCafeClicked = onCafeClicked,
                        onOrderClicked = onOrderClicked,
                    )
                }
            }
        }
    }

    @Composable
    private fun SuccessOrderListScreen(
        uiStateSuccess: OrderListUiState.State.Success,
        onCafeClicked: () -> Unit,
        onOrderClicked: (OrderListUiState.OrderItem) -> Unit,
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(key = "cafeAddress") {
                NavigationTextCard(
                    hintStringId = R.string.hint_orders_cafe,
                    label = uiStateSuccess.cafeAddress,
                    onClick = onCafeClicked
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(
                items = uiStateSuccess.orderList,
                key = { orderItem -> orderItem.uuid }
            ) { orderItem ->
                OrderItem(orderItem = orderItem, onClick = onOrderClicked)
            }
        }
    }

    private fun handleEventList(eventList: List<OrderListEvent>) {
        eventList.forEach { event ->
            when (event) {
                is OrderListEvent.ScrollToTop -> {
//                    lifecycleScope.launch {
//                        delay(500)
//                        binding.orderListRv.smoothScrollToPosition(0)
//                    }
                }
                is OrderListEvent.OpenCafeListEvent -> {
                    openCafeList(event.cafeList)
                }
                is OrderListEvent.OpenOrderDetailsEvent -> {
                    openOrderDetails(event.orderUuid, event.orderCode)
                }
                OrderListEvent.OpenLoginEvent -> {
                    findNavController().navigateSafe(toLoginFragment())
                }
                OrderListEvent.ShowError -> {
                    lifecycleScope.launch {
                        ErrorDialog.show(childFragmentManager).let {
                            viewModel.onRetryClicked()
                        }
                    }
                }
                OrderListEvent.CancelNotification -> {
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
