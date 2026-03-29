package com.bunbeauty.presentation.feature.order

import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.presentation.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.presentation.designsystem.compose.element.button.SecondaryButton
import com.bunbeauty.presentation.designsystem.compose.element.card.AdminCard
import com.bunbeauty.presentation.designsystem.compose.element.card.DiscountCard
import com.bunbeauty.presentation.designsystem.compose.element.card.NavigationIconCard
import com.bunbeauty.presentation.designsystem.compose.element.card.StatusNavigationTextCard
import com.bunbeauty.presentation.designsystem.compose.element.selectable.SelectableItem
import com.bunbeauty.presentation.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.presentation.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.presentation.designsystem.compose.theme.bold
import com.bunbeauty.presentation.designsystem.compose.theme.medium
import com.bunbeauty.presentation.feature.order.navigation.OrderDetailsScreenDestination
import com.bunbeauty.presentation.feature.order.state.OrderDetailsState
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_order_details_do_not_save
import fooddeliveryadmin.presentation.generated.resources.action_order_details_no
import fooddeliveryadmin.presentation.generated.resources.action_order_details_save
import fooddeliveryadmin.presentation.generated.resources.action_order_details_yes
import fooddeliveryadmin.presentation.generated.resources.hint_order_details_order_status
import fooddeliveryadmin.presentation.generated.resources.ic_call
import fooddeliveryadmin.presentation.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.presentation.generated.resources.msg_order_details_address
import fooddeliveryadmin.presentation.generated.resources.msg_order_details_alert
import fooddeliveryadmin.presentation.generated.resources.msg_order_details_comment
import fooddeliveryadmin.presentation.generated.resources.msg_order_details_delivery_cost
import fooddeliveryadmin.presentation.generated.resources.msg_order_details_discount_cost
import fooddeliveryadmin.presentation.generated.resources.msg_order_details_order_cost
import fooddeliveryadmin.presentation.generated.resources.msg_order_details_order_time
import fooddeliveryadmin.presentation.generated.resources.msg_order_details_payment_method
import fooddeliveryadmin.presentation.generated.resources.msg_order_details_pickup_method
import fooddeliveryadmin.presentation.generated.resources.msg_order_details_saved
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.presentation.generated.resources.title_order_details_alert
import fooddeliveryadmin.presentation.generated.resources.title_order_status
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OrderDetailsRouteScreen(
    viewModel: OrderDetailsViewModel = koinViewModel(),
    showInfoMessage: (String, Dp) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    backStackEntry: NavBackStackEntry,
) {
    val route = backStackEntry.toRoute<OrderDetailsScreenDestination>()
    val onCallPhone = rememberPhoneDialerLauncher()
    var isCancellationWarningShown by remember {
        mutableStateOf(false)
    }
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: OrderDetailsState.Action ->
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

    LaunchedEffect(Unit) {
        onAction(
            OrderDetailsState.Action.Init(
                orderUuid = route.orderUuid,
                orderCode = route.orderCode,
            ),
        )
    }

    OrderDetailsEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        showErrorMessage = showErrorMessage,
        goBack = goBack,
        onShowCancellationWarning = {
            isCancellationWarningShown = true
        },
    )

    OrderDetailsScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        onCallPhone = onCallPhone,
    )

    CancellationWarningDialog(
        isShown = isCancellationWarningShown,
        onDismiss = {
            isCancellationWarningShown = false
        },
        onConfirm = {
            isCancellationWarningShown = false
            viewModel.onCancellationConfirmed()
        },
    )
}

@Composable
private fun OrderDetailsEffect(
    effects: List<OrderDetailsState.Event>,
    showInfoMessage: (String, Dp) -> Unit,
    showErrorMessage: (String) -> Unit,
    goBack: () -> Unit,
    onShowCancellationWarning: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                OrderDetailsState.Event.OpenWarningDialogEvent -> {
                    onShowCancellationWarning()
                }

                is OrderDetailsState.Event.ShowErrorMessage -> {
                    showErrorMessage("Ошибка")
                }

                OrderDetailsState.Event.GoBackEvent -> {
                    goBack()
                }

                is OrderDetailsState.Event.SavedEvent -> {
                    showInfoMessage(
                        getString(
                            Res.string.msg_order_details_saved,
                            effect.orderCode
                        ),
                        0.dp
                    )
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun OrderDetailsScreen(
    state: OrderDetailsViewState,
    onAction: (OrderDetailsState.Action) -> Unit,
    onCallPhone: (String) -> Unit,
) {
    AdminScaffold(
        title = state.title,
        backActionClick = {
            onAction(OrderDetailsState.Action.OnBackClicked)
        },
    ) {
        when (state.state) {
            OrderDetailsViewState.State.Loading -> {
                LoadingScreen()
            }

            OrderDetailsViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {},
                )
            }

            is OrderDetailsViewState.State.Success -> {
                SuccessOrderDetailsScreen(
                    state = state.state,
                    onAction = onAction,
                    onCallPhone = onCallPhone,
                )
            }
        }
    }
}

@Composable
private fun SuccessOrderDetailsScreen(
    state: OrderDetailsViewState.State.Success,
    onAction: (OrderDetailsState.Action) -> Unit,
    onCallPhone: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(AdminTheme.dimensions.screenContentSpace),
            ) {
                item {
                    OrderInfoCard(stateSuccess = state)
                }
                item {
                    StatusNavigationTextCard(
                        hintStringId = Res.string.hint_order_details_order_status,
                        label = state.status,
                        onClick = {
                            onAction(OrderDetailsState.Action.OnStatusClicked)
                        },
                        statusColor = state.statusColor,
                    )
                }
                item {
                    NavigationIconCard(
                        iconId = Res.drawable.ic_call,
                        label = state.phoneNumber,
                        onClick = {
                            onCallPhone(state.phoneNumber)
                        },
                    )
                }

                items(state.productList) { product ->
                    OrderProductItem(product = product)
                }
            }
        }
        BottomAmountBar(
            stateSuccess = state,
            onAction = onAction,
        )
        StatusListBottomSheet(
            state = state,
            onAction = onAction,
        )
    }
}

@Composable
private fun CancellationWarningDialog(
    isShown: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (!isShown) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(Res.string.title_order_details_alert))
        },
        text = {
            Text(text = stringResource(Res.string.msg_order_details_alert))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(Res.string.action_order_details_yes))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.action_order_details_no))
            }
        },
    )
}

@Composable
private fun StatusListBottomSheet(
    state: OrderDetailsViewState.State.Success,
    onAction: (OrderDetailsState.Action) -> Unit,
) {
    AdminModalBottomSheet(
        title = stringResource(Res.string.title_order_status),
        isShown = state.statusListUI.isShown,
        onDismissRequest = {
            onAction(OrderDetailsState.Action.OnCloseStatusClicked)
        },
        content = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
            ) {
                state.statusListUI.statusList.forEach { status ->
                    SelectableItem(
                        title = status.status,
                        clickable = true,
                        elevated = false,
                        onClick = {
                            onAction(
                                OrderDetailsState.Action.OnSelectStatusClicked(
                                    status = status.orderStatus,
                                ),
                            )
                        },
                    )
                }
            }
        },
    )
}

@Composable
private fun OrderInfoCard(
    modifier: Modifier = Modifier,
    stateSuccess: OrderDetailsViewState.State.Success,
) {
    AdminCard(
        modifier = modifier,
        clickable = false,
        elevated = false,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                OrderInfoTextColumn(
                    modifier = Modifier.weight(1f),
                    hint = stringResource(Res.string.msg_order_details_order_time),
                    info = stateSuccess.dateTime,
                )
                stateSuccess.deferredTime?.let { deferredTime ->
                    OrderInfoTextColumn(
                        modifier =
                            Modifier
                                .padding(start = 16.dp)
                                .weight(1f),
                        hint = deferredTime.hint,
                        info = deferredTime.value,
                    )
                }
            }
            Row {
                OrderInfoTextColumn(
                    modifier = Modifier.weight(1f),
                    hint = stringResource(Res.string.msg_order_details_pickup_method),
                    info = stateSuccess.receiptMethod,
                )
                stateSuccess.paymentMethod?.let { paymentMethod ->
                    OrderInfoTextColumn(
                        modifier =
                            Modifier
                                .padding(start = 16.dp)
                                .weight(1f),
                        hint = stringResource(Res.string.msg_order_details_payment_method),
                        info = paymentMethod,
                    )
                }
            }
            OrderInfoTextColumn(
                hint = stringResource(Res.string.msg_order_details_address),
                info = stateSuccess.address,
            )
            stateSuccess.comment?.let { comment ->
                OrderInfoTextColumn(
                    hint = stringResource(Res.string.msg_order_details_comment),
                    info = comment,
                )
            }
        }
    }
}

@Composable
private fun OrderInfoTextColumn(
    modifier: Modifier = Modifier,
    hint: String,
    info: String,
) {
    Column(modifier = modifier) {
        Text(
            text = hint,
            style = AdminTheme.typography.labelSmall.medium,
            color = AdminTheme.colors.main.onSurfaceVariant,
        )
        Text(
            text = info,
            style = AdminTheme.typography.bodyMedium,
            color = AdminTheme.colors.main.onSurface,
        )
    }
}

@Composable
private fun BottomAmountBar(
    stateSuccess: OrderDetailsViewState.State.Success,
    onAction: (OrderDetailsState.Action) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(AdminTheme.colors.main.surface)
                .padding(16.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            stateSuccess.percentDiscount?.let { discount ->
                Row {
                    Text(
                        text = stringResource(Res.string.msg_order_details_discount_cost),
                        style = AdminTheme.typography.bodyMedium,
                        color = AdminTheme.colors.main.onSurface,
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    DiscountCard(discount = discount)
                }
            }
            stateSuccess.deliveryCost?.let { deliveryCost ->
                Row {
                    Text(
                        text = stringResource(Res.string.msg_order_details_delivery_cost),
                        style = AdminTheme.typography.bodyMedium,
                        color = AdminTheme.colors.main.onSurface,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        modifier = Modifier.weight(1f),
                        text = deliveryCost,
                        style = AdminTheme.typography.bodyMedium,
                        color = AdminTheme.colors.main.onSurface,
                        textAlign = TextAlign.End,
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(Res.string.msg_order_details_order_cost),
                    style = AdminTheme.typography.bodyMedium.bold,
                    color = AdminTheme.colors.main.onSurface,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stateSuccess.finalCost,
                    style = AdminTheme.typography.bodyMedium.bold,
                    color = AdminTheme.colors.main.onSurface,
                )
            }
        }

        LoadingButton(
            modifier = Modifier.padding(top = 16.dp),
            text = stringResource(Res.string.action_order_details_save),
            isLoading = stateSuccess.saving,
            onClick = {
                onAction(OrderDetailsState.Action.OnSaveClicked)
            },
        )
        SecondaryButton(
            modifier = Modifier.padding(top = 8.dp),
            textStringId = Res.string.action_order_details_do_not_save,
            onClick = {
                onAction(OrderDetailsState.Action.OnBackClicked)
            },
        )
    }
}
