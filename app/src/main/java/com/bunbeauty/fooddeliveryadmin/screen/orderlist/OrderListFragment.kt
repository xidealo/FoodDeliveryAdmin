package com.bunbeauty.fooddeliveryadmin.screen.orderlist

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseFragment
import com.bunbeauty.fooddeliveryadmin.databinding.LayoutComposeBinding
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.fooddeliveryadmin.notification.LAST_ORDER_NOTIFICATION_ID
import com.bunbeauty.fooddeliveryadmin.screen.cafelist.CafeListBottomSheet
import com.bunbeauty.fooddeliveryadmin.screen.error.ErrorDialog
import com.bunbeauty.fooddeliveryadmin.screen.orderlist.OrderListFragmentDirections.Companion.toLoginFragment
import com.bunbeauty.fooddeliveryadmin.screen.orderlist.OrderListFragmentDirections.Companion.toOrdersDetailsFragment
import com.bunbeauty.presentation.feature.cafelist.SelectableCafeItem
import com.bunbeauty.presentation.feature.orderlist.OrderListViewModel
import com.bunbeauty.presentation.feature.orderlist.state.OrderListEvent
import com.bunbeauty.presentation.feature.orderlist.state.OrderListUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderListFragment : BaseFragment<LayoutComposeBinding>() {

    @Inject
    lateinit var notificationManagerCompat: NotificationManagerCompat

    override val viewModel: OrderListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(viewModel)

        binding.root.setContentWithTheme {
            val uiState by viewModel.orderListUiState.collectAsStateWithLifecycle()
            val lazyListState = rememberLazyListState()
            OrderListScreen(
                uiState = uiState,
                lazyListState = lazyListState,
                onRefresh = viewModel::onRefresh,
                onCafeClicked = viewModel::onCafeClicked,
                onOrderClicked = viewModel::onOrderClicked
            )

            val scope = rememberCoroutineScope()
            LaunchedEffect(uiState.eventList) {
                handleEventList(
                    lazyListState = lazyListState,
                    scope = scope,
                    eventList = uiState.eventList
                )
            }
        }
    }

    @Composable
    private fun OrderListScreen(
        uiState: OrderListUiState,
        lazyListState: LazyListState,
        onRefresh: () -> Unit,
        onCafeClicked: () -> Unit,
        onOrderClicked: (OrderListUiState.OrderItem) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_orders),
            pullRefreshEnabled = uiState.state is OrderListUiState.State.Success,
            refreshing = uiState.refreshing,
            onRefresh = onRefresh
        ) {
            Column {
                if (uiState.connectionError) {
                    ConnectionError()
                }
                when (val state = uiState.state) {
                    OrderListUiState.State.Loading -> {
                        LoadingScreen()
                    }

                    OrderListUiState.State.Error -> {
                        ErrorScreen(
                            mainTextId = R.string.title_common_can_not_load_data,
                            extraTextId = R.string.msg_common_check_connection_and_retry,
                            onClick = viewModel::retrySetUp
                        )
                    }

                    is OrderListUiState.State.Success -> {
                        SuccessOrderListScreen(
                            uiStateSuccess = state,
                            lazyListState = lazyListState,
                            onCafeClicked = onCafeClicked,
                            onOrderClicked = onOrderClicked
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun ConnectionError() {
        Box(
            modifier = Modifier
                .background(AdminTheme.colors.status.negative)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.error_order_list_connection),
                style = AdminTheme.typography.bodySmall,
                color = AdminTheme.colors.status.onStatus,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    private fun SuccessOrderListScreen(
        uiStateSuccess: OrderListUiState.State.Success,
        lazyListState: LazyListState,
        onCafeClicked: () -> Unit,
        onOrderClicked: (OrderListUiState.OrderItem) -> Unit
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(key = "cafeAddress") {
                NavigationTextCard(
                    hintStringId = R.string.msg_common_cafe,
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

    private fun handleEventList(
        eventList: List<OrderListEvent>,
        scope: CoroutineScope,
        lazyListState: LazyListState
    ) {
        eventList.forEach { event ->
            when (event) {
                is OrderListEvent.ScrollToTop -> {
                    scope.launch {
                        delay(100)
                        lazyListState.animateScrollToItem(0)
                    }
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
                    scope.launch {
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

    private fun openCafeList(cafeList: List<SelectableCafeItem>) {
        lifecycleScope.launch {
            val selectedCafe = SelectCafeBottomSheet.show(
                parentFragmentManager,
                cafeList
            )
            viewModel.onCafeSelected(selectedCafe?.uuid)
        }
    }

    private fun openOrderDetails(orderUuid: String, orderCode: String) {
        findNavController().navigateSafe(toOrdersDetailsFragment(orderUuid, orderCode))
    }
}
