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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationTextCard
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeListFragment
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.fooddeliveryadmin.screen.orderlist.OrderListFragmentDirections.Companion.toOrdersDetailsFragment
import com.bunbeauty.fooddeliveryadmin.screen.orderlist.compose.OrderItem
import com.bunbeauty.presentation.feature.orderlist.OrderListViewModel
import com.bunbeauty.presentation.feature.orderlist.state.OrderList
import com.bunbeauty.presentation.feature.selectcafe.SelectableCafeItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderListFragment :
    BaseComposeListFragment<OrderList.DataState, OrderListViewState, OrderList.Action, OrderList.Event>() {

    @Inject
    lateinit var notificationManagerCompat: NotificationManagerCompat

    @Inject
    lateinit var orderMapper: OrderMapper

    override val viewModel: OrderListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onAction(OrderList.Action.StartObserveOrders)
    }

    @Composable
    override fun Screen(
        state: OrderListViewState,
        lazyListState: LazyListState,
        onAction: (OrderList.Action) -> Unit
    ) {
        AdminScaffold(
            title = stringResource(R.string.title_orders),
            pullRefreshEnabled = state.state is OrderListViewState.State.Success,
            refreshing = if (state.state is OrderListViewState.State.Success) {
                state.state.refreshing
            } else {
                false
            },
            onRefresh = {
                onAction(OrderList.Action.RefreshSwipe)
            }
        ) {
            when (state.state) {
                OrderListViewState.State.Loading -> {
                    LoadingScreen()
                }

                OrderListViewState.State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {
                            //retry click
                        }
                    )
                }

                is OrderListViewState.State.Success -> {
                    SuccessOrderListScreen(
                        state = state.state,
                        lazyListState = lazyListState,
                        onAction = onAction,
                    )
                }
            }

        }
    }

    @Composable
    override fun mapState(state: OrderList.DataState): OrderListViewState {
        return OrderListViewState(
            state = when (state.cafeState) {
                OrderList.DataState.State.LOADING -> OrderListViewState.State.Loading
                OrderList.DataState.State.ERROR -> OrderListViewState.State.Error
                OrderList.DataState.State.SUCCESS -> OrderListViewState.State.Success(
                    cafeAddress = state.selectedCafe?.address.orEmpty(),
                    orderList = state.orderList.map(orderMapper::map).toPersistentList(),
                    connectionError = state.orderListState == OrderList.DataState.State.ERROR,
                    refreshing = state.refreshing,
                )
            },
        )
    }

    override fun handleEvent(
        event: OrderList.Event,
        lazyListState: LazyListState,
        coroutineScope: CoroutineScope
    ) {
        when (event) {
            is OrderList.Event.CancelNotification -> {
                notificationManagerCompat.cancel(event.notificationId)
            }

            is OrderList.Event.OpenOrderDetailsEvent -> openOrderDetails(
                event.orderUuid,
                event.orderCode
            )

            OrderList.Event.ScrollToTop -> {
                coroutineScope.launch {
                    delay(100)
                    lazyListState.animateScrollToItem(0)
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
        state: OrderListViewState.State.Success,
        lazyListState: LazyListState,
        onAction: (OrderList.Action) -> Unit
    ) {
        Column {
            if (state.connectionError) {
                ConnectionError()
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(key = "cafeAddress") {
                    NavigationTextCard(
                        labelText = stringResource(R.string.msg_common_cafe),
                        valueText = state.cafeAddress,
                        onClick = { onAction(OrderList.Action.CafeClick) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(
                    items = state.orderList,
                    key = { orderItem -> orderItem.uuid }
                ) { orderItem ->
                    OrderItem(
                        orderItem = orderItem,
                        onClick = {
                            onAction(
                                OrderList.Action.OrderClick(
                                    orderCode = orderItem.code,
                                    orderUuid = orderItem.uuid
                                )
                            )
                        }
                    )
                }
            }
        }

    }

    private fun openCafeList(cafeList: List<SelectableCafeItem>) {
        lifecycleScope.launch {
//            val selectedCafe = SelectCafeBottomSheet.show(
//                parentFragmentManager,
//                cafeList
//            )
//            viewModel.onCafeSelected(selectedCafe?.uuid)
        }
    }

    private fun openOrderDetails(orderUuid: String, orderCode: String) {
        findNavController().navigateSafe(toOrdersDetailsFragment(orderUuid, orderCode))
    }
}
