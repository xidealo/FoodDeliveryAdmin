package com.bunbeauty.presentation.feature.orderlist

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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.card.TextWithHintCard
import com.bunbeauty.presentation.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.feature.orderlist.compose.OrderItem
import com.bunbeauty.presentation.feature.orderlist.state.OrderList
import com.bunbeauty.presentation.feature.orderlist.state.OrderListViewState
import com.bunbeauty.presentation.feature.orderlist.state.OrderMapper
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.error_order_list_connection
import fooddeliveryadmin.presentation.generated.resources.msg_common_cafe
import fooddeliveryadmin.presentation.generated.resources.title_orders
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

const val CAFE_ADDRESS_KEY = "cafeAddress"

@Composable
fun OrderList.DataState.mapStateOrderList(
    orderMapper: OrderMapper = koinInject()
): OrderListViewState =
    OrderListViewState(
        state =
            when (orderListState) {
                OrderList.DataState.State.LOADING -> OrderListViewState.State.Loading
                OrderList.DataState.State.SUCCESS ->
                    OrderListViewState.State.Success(
                        cafeAddress = cafe?.address.orEmpty(),
                        orderList = orderList.map { order ->
                            orderMapper.map(order)
                        }.toPersistentList(),
                        connectionError = hasConnectionError,
                        refreshing = refreshing,
                        loadingOrderList = loadingOrderList,
                    )
            },
    )

@Composable
fun OrderList.DataState.mapState(): OrderListViewState = mapStateOrderList()

@Composable
fun OrderListRouteScreen(
    viewModel: OrderListViewModel = koinViewModel(),
    cancelNotification: (Int) -> Unit,
    openOrderDetails: (String, String) -> Unit,
) {

    LifecycleStartEffect(Unit) {
        viewModel.onAction(OrderList.Action.StartObserveOrders)
        onStopOrDispose {
            viewModel.onAction(OrderList.Action.StopObserveOrders)
        }
    }

    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: OrderList.Action ->
                viewModel.onAction(event)
            }
        }

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }

    OrderListEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        cancelNotification = cancelNotification,
        openOrderDetails = openOrderDetails,
    )
    OrderListScreen(
        state = viewState.mapState(),
        lazyListState = rememberLazyListState(),
        onAction = onAction,
    )

}

@Composable
private fun OrderListEffect(
    effects: List<OrderList.Event>,
    cancelNotification: (Int) -> Unit,
    openOrderDetails: (String, String) -> Unit,
    consumeEffects: () -> Unit,
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                is OrderList.Event.CancelNotification -> {
                    cancelNotification(effect.notificationId)
                }

                is OrderList.Event.OpenOrderDetailsEvent -> openOrderDetails(
                    effect.orderCode,
                    effect.orderUuid
                )

                OrderList.Event.ScrollToTop -> {
                    coroutineScope {
                        launch {
                            delay(100)
                            lazyListState.animateScrollToItem(0)
                        }
                    }
                }
            }
            consumeEffects()
        }
    }
}


@Composable
private fun OrderListScreen(
    state: OrderListViewState,
    lazyListState: LazyListState,
    onAction: (OrderList.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_orders),
        //  pullRefreshEnabled = state.state is OrderListViewState.State.Success,
        onRefresh = {
            onAction(OrderList.Action.RefreshSwipe)
        },
    ) {
        when (state.state) {
            OrderListViewState.State.Loading -> {
                LoadingScreen()
            }

            is OrderListViewState.State.Success -> {
                OrderListSuccessScreen(
                    state = state.state,
                    lazyListState = lazyListState,
                    onAction = onAction,
                )
            }
        }
    }
}


@Composable
private fun ConnectionError() {
    Box(
        modifier =
            Modifier
                .background(AdminTheme.colors.status.negative)
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp,
                ),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.error_order_list_connection),
            style = AdminTheme.typography.bodySmall,
            color = AdminTheme.colors.status.onStatus,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun OrderListSuccessScreen(
    state: OrderListViewState.State.Success,
    lazyListState: LazyListState,
    onAction: (OrderList.Action) -> Unit,
) {
    Column {
        if (state.connectionError) {
            ConnectionError()
        }

        if (state.loadingOrderList) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = AdminTheme.colors.main.primary,
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item(key = CAFE_ADDRESS_KEY) {
                TextWithHintCard(
                    hint = stringResource(Res.string.msg_common_cafe),
                    label = state.cafeAddress,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(
                items = state.orderList,
                key = { orderItem -> orderItem.uuid },
            ) { orderItem ->
                OrderItem(
                    orderItem = orderItem,
                    onClick = {
                        onAction(
                            OrderList.Action.OrderClick(
                                orderCode = orderItem.code,
                                orderUuid = orderItem.uuid,
                            ),
                        )
                    },
                )
            }
        }
    }
}

@Preview()
@Composable
private fun OrderListSuccessScreenPreview() {
    AdminTheme {
        OrderListSuccessScreen(
            state =
                OrderListViewState.State.Success(
                    cafeAddress = "Кафе сатаны",
                    orderList =
                        persistentListOf(
                            OrderListViewState.OrderItem(
                                uuid = "1",
                                status = OrderStatus.ACCEPTED,
                                statusString = "Принят",
                                code = "22",
                                deferredTime = "",
                                dateTime = "12/9/2024",
                            ),
                        ),
                    connectionError = false,
                    refreshing = false,
                    loadingOrderList = false,
                ),
            lazyListState = LazyListState(),
            onAction = {},
        )
    }
}

