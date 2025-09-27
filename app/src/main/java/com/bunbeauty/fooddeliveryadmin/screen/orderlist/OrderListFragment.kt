package com.bunbeauty.fooddeliveryadmin.screen.orderlist

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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.findNavController
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.card.TextWithHintCard
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeListFragment
import com.bunbeauty.fooddeliveryadmin.navigation.navigateSafe
import com.bunbeauty.fooddeliveryadmin.screen.orderlist.OrderListFragmentDirections.Companion.toOrdersDetailsFragment
import com.bunbeauty.fooddeliveryadmin.screen.orderlist.compose.OrderItem
import com.bunbeauty.presentation.feature.orderlist.OrderListViewModel
import com.bunbeauty.presentation.feature.orderlist.state.OrderList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

const val CAFE_ADDRESS_KEY = "cafeAddress"

class OrderListFragment :
    BaseComposeListFragment<OrderList.DataState, OrderListViewState, OrderList.Action, OrderList.Event>() {

    private val notificationManagerCompat: NotificationManagerCompat by inject()

    override val viewModel: OrderListViewModel by viewModel()

    private val orderMapper: OrderMapper by inject()

    override fun onStart() {
        super.onStart()
        viewModel.onAction(OrderList.Action.StartObserveOrders)
    }

    override fun onStop() {
        viewModel.onAction(OrderList.Action.StopObserveOrders)
        super.onStop()
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
            onRefresh = {
                onAction(OrderList.Action.RefreshSwipe)
            }
        ) {
            when (state.state) {
                OrderListViewState.State.Loading -> {
                    LoadingScreen()
                }

                is OrderListViewState.State.Success -> {
                    OrderListSuccessScreen(
                        state = state.state,
                        lazyListState = lazyListState,
                        onAction = onAction
                    )
                }
            }
        }
    }

    @Composable
    override fun mapState(state: OrderList.DataState): OrderListViewState {
        return OrderListViewState(
            state = when (state.orderListState) {
                OrderList.DataState.State.LOADING -> OrderListViewState.State.Loading
                OrderList.DataState.State.SUCCESS -> OrderListViewState.State.Success(
                    cafeAddress = state.cafe?.address.orEmpty(),
                    orderList = state.orderList.map(orderMapper::map).toPersistentList(),
                    connectionError = state.hasConnectionError,
                    refreshing = state.refreshing,
                    loadingOrderList = state.loadingOrderList
                )
            }
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
    private fun OrderListSuccessScreen(
        state: OrderListViewState.State.Success,
        lazyListState: LazyListState,
        onAction: (OrderList.Action) -> Unit
    ) {
        Column {
            if (state.connectionError) {
                ConnectionError()
            }

            if (state.loadingOrderList) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = AdminTheme.colors.main.primary
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(key = CAFE_ADDRESS_KEY) {
                    TextWithHintCard(
                        hint = stringResource(R.string.msg_common_cafe),
                        label = state.cafeAddress
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

    private fun openOrderDetails(orderUuid: String, orderCode: String) {
        findNavController().navigateSafe(toOrdersDetailsFragment(orderUuid, orderCode))
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun OrderListSuccessScreenPreview() {
        AdminTheme {
            OrderListSuccessScreen(
                state = OrderListViewState.State.Success(
                    cafeAddress = "Кафе сатаны",
                    orderList = persistentListOf(
                        OrderListViewState.OrderItem(
                            uuid = "1",
                            status = OrderStatus.ACCEPTED,
                            statusString = "Принят",
                            code = "22",
                            deferredTime = "",
                            dateTime = "12/9/2024"
                        )
                    ),
                    connectionError = false,
                    refreshing = false,
                    loadingOrderList = false
                ),
                lazyListState = LazyListState(),
                onAction = {}
            )
        }
    }
}
