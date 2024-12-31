package com.bunbeauty.fooddeliveryadmin.screen.orderdetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.button.SecondaryButton
import com.bunbeauty.fooddeliveryadmin.compose.element.card.AdminCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.DiscountCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.NavigationIconCard
import com.bunbeauty.fooddeliveryadmin.compose.element.card.StatusNavigationTextCard
import com.bunbeauty.fooddeliveryadmin.compose.element.selectable.SelectableItem
import com.bunbeauty.fooddeliveryadmin.compose.screen.ErrorScreen
import com.bunbeauty.fooddeliveryadmin.compose.screen.LoadingScreen
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.compose.theme.bold
import com.bunbeauty.fooddeliveryadmin.compose.theme.medium
import com.bunbeauty.fooddeliveryadmin.coreui.BaseComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.presentation.feature.order.OrderDetailsViewModel
import com.bunbeauty.presentation.feature.order.state.OrderDetailsState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.collections.immutable.persistentListOf
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val PHONE_LINK = "tel:"

class OrderDetailsFragment :
    BaseComposeFragment<OrderDetailsState.DataState, OrderDetailsViewState, OrderDetailsState.Action, OrderDetailsState.Event>() {

    private val orderDetailsFragmentArgs: OrderDetailsFragmentArgs by navArgs()

    override val viewModel: OrderDetailsViewModel by viewModel()

    private val orderDetailsStateMapper: OrderDetailsStateMapper by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onAction(
            OrderDetailsState.Action.Init(
                orderUuid = orderDetailsFragmentArgs.orderUuid,
                orderCode = orderDetailsFragmentArgs.orderCode
            )
        )
    }

    @Composable
    override fun Screen(
        state: OrderDetailsViewState,
        onAction: (OrderDetailsState.Action) -> Unit
    ) {
        OrderDetailsScreen(
            state = state,
            onAction = onAction
        )
    }

    @Composable
    override fun mapState(state: OrderDetailsState.DataState): OrderDetailsViewState {
        return orderDetailsStateMapper.map(state)
    }

    override fun handleEvent(event: OrderDetailsState.Event) {
        when (event) {
            OrderDetailsState.Event.OpenWarningDialogEvent -> {
                showCancellationWarning()
            }

            is OrderDetailsState.Event.ShowErrorMessage -> {
                (activity as? MessageHost)?.showErrorMessage(
                    resources.getString(event.messageId)
                )
            }

            is OrderDetailsState.Event.GoBackEvent -> {
                findNavController().navigateUp()
            }

            is OrderDetailsState.Event.SavedEvent -> {
                (activity as? MessageHost)?.showInfoMessage(
                    getString(
                        R.string.msg_order_details_saved,
                        event.orderCode
                    )
                )
                findNavController().navigateUp()
            }
        }
    }

    @Composable
    private fun OrderDetailsScreen(
        state: OrderDetailsViewState,
        onAction: (OrderDetailsState.Action) -> Unit
    ) {
        AdminScaffold(
            title = state.title,
            backActionClick = {
                onAction(OrderDetailsState.Action.OnBackClicked)
            }
        ) {
            when (state.state) {
                OrderDetailsViewState.State.Loading -> {
                    LoadingScreen()
                }

                OrderDetailsViewState.State.Error -> {
                    ErrorScreen(
                        mainTextId = R.string.title_common_can_not_load_data,
                        extraTextId = R.string.msg_common_check_connection_and_retry,
                        onClick = {}
                    )
                }

                is OrderDetailsViewState.State.Success -> {
                    SuccessOrderDetailsScreen(
                        state = state.state,
                        onAction = onAction
                    )
                }
            }
        }
    }

    @Composable
    private fun SuccessOrderDetailsScreen(
        state: OrderDetailsViewState.State.Success,
        onAction: (OrderDetailsState.Action) -> Unit
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(AdminTheme.dimensions.screenContentSpace)
                ) {
                    item {
                        OrderInfoCard(stateSuccess = state)
                    }
                    item {
                        StatusNavigationTextCard(
                            hintStringId = R.string.hint_order_details_order_status,
                            label = state.status,
                            onClick = {
                                onAction(OrderDetailsState.Action.OnStatusClicked)
                            },
                            statusColor = state.statusColor
                        )
                    }
                    item {
                        NavigationIconCard(
                            iconId = R.drawable.ic_call,
                            label = state.phoneNumber,
                            onClick = {
                                val uri = Uri.parse(PHONE_LINK + state.phoneNumber)
                                val intent = Intent(Intent.ACTION_DIAL, uri)
                                startActivity(intent)
                            }
                        )
                    }

                    items(state.productList) { product ->
                        OrderProductItem(product = product)
                    }
                }
            }
            BottomAmountBar(
                stateSuccess = state,
                onAction = onAction
            )
            CafeListBottomSheet(state = state, onAction = onAction)
        }
    }

    @Composable
    private fun CafeListBottomSheet(
        state: OrderDetailsViewState.State.Success,
        onAction: (OrderDetailsState.Action) -> Unit
    ) {
        AdminModalBottomSheet(
            title = stringResource(R.string.title_order_list_select_cafe),
            isShown = state.statusListUI.isShown,
            onDismissRequest = {
                onAction(OrderDetailsState.Action.OnCloseStatusClicked)
            }
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                state.statusListUI.statusList.forEach { status ->
                    SelectableItem(
                        title = status.status,
                        clickable = true,
                        elevated = false,
                        onClick = {
                            onAction(
                                OrderDetailsState.Action.OnSelectStatusClicked(
                                    status = status.orderStatus
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    @Composable
    private fun OrderInfoCard(
        modifier: Modifier = Modifier,
        stateSuccess: OrderDetailsViewState.State.Success
    ) {
        AdminCard(
            modifier = modifier,
            clickable = false,
            elevated = false
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OrderInfoTextColumn(
                        modifier = Modifier.weight(1f),
                        hint = stringResource(R.string.msg_order_details_order_time),
                        info = stateSuccess.dateTime
                    )
                    stateSuccess.deferredTime?.let { deferredTime ->
                        OrderInfoTextColumn(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f),
                            hint = deferredTime.hint,
                            info = deferredTime.value
                        )
                    }
                }
                Row {
                    OrderInfoTextColumn(
                        modifier = Modifier.weight(1f),
                        hint = stringResource(R.string.msg_order_details_pickup_method),
                        info = stateSuccess.receiptMethod
                    )
                    stateSuccess.paymentMethod?.let { paymentMethod ->
                        OrderInfoTextColumn(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f),
                            hint = stringResource(R.string.msg_order_details_payment_method),
                            info = paymentMethod
                        )
                    }
                }
                OrderInfoTextColumn(
                    hint = stringResource(R.string.msg_order_details_address),
                    info = stateSuccess.address
                )
                stateSuccess.comment?.let { comment ->
                    OrderInfoTextColumn(
                        hint = stringResource(R.string.msg_order_details_comment),
                        info = comment
                    )
                }
            }
        }
    }

    @Composable
    private fun OrderInfoTextColumn(
        modifier: Modifier = Modifier,
        hint: String,
        info: String
    ) {
        Column(modifier = modifier) {
            Text(
                text = hint,
                style = AdminTheme.typography.labelSmall.medium,
                color = AdminTheme.colors.main.onSurfaceVariant
            )
            Text(
                text = info,
                style = AdminTheme.typography.bodyMedium,
                color = AdminTheme.colors.main.onSurface
            )
        }
    }

    @Composable
    private fun BottomAmountBar(
        stateSuccess: OrderDetailsViewState.State.Success,
        onAction: (OrderDetailsState.Action) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(AdminTheme.colors.main.surface)
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = spacedBy(8.dp)
            ) {
                stateSuccess.percentDiscount?.let { discount ->
                    Row {
                        Text(
                            text = stringResource(R.string.msg_order_details_discount_cost),
                            style = AdminTheme.typography.bodyMedium,
                            color = AdminTheme.colors.main.onSurface
                        )
                        Spacer(modifier = Modifier.weight(1f))

                        DiscountCard(discount = discount)
                    }
                }
                stateSuccess.deliveryCost?.let { deliveryCost ->
                    Row {
                        Text(
                            text = stringResource(R.string.msg_order_details_delivery_cost),
                            style = AdminTheme.typography.bodyMedium,
                            color = AdminTheme.colors.main.onSurface
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            modifier = Modifier.weight(1f),
                            text = deliveryCost,
                            style = AdminTheme.typography.bodyMedium,
                            color = AdminTheme.colors.main.onSurface,
                            textAlign = TextAlign.End
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.msg_order_details_order_cost),
                        style = AdminTheme.typography.bodyMedium.bold,
                        color = AdminTheme.colors.main.onSurface
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = stateSuccess.finalCost,
                        style = AdminTheme.typography.bodyMedium.bold,
                        color = AdminTheme.colors.main.onSurface
                    )
                }
            }

            LoadingButton(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(R.string.action_order_details_save),
                isLoading = stateSuccess.saving,
                onClick = {
                    onAction(OrderDetailsState.Action.OnSaveClicked)
                }
            )
            SecondaryButton(
                modifier = Modifier.padding(top = 8.dp),
                textStringId = R.string.action_order_details_do_not_save,
                onClick = {
                    onAction(OrderDetailsState.Action.OnBackClicked)
                }
            )
        }
    }

    private fun showCancellationWarning() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_order_details_alert)
            .setMessage(R.string.msg_order_details_alert)
            .setPositiveButton(R.string.action_order_details_yes) { _, _ ->
                viewModel.onCancellationConfirmed()
            }
            .setNegativeButton(R.string.action_order_details_no) { _, _ -> }
            .show()
    }

    @Preview(showSystemUi = true)
    @Composable
    private fun OrderDetailsScreenPreview() {
        AdminTheme {
            OrderDetailsScreen(
                state = OrderDetailsViewState(
                    title = "Заказ «А-10»",
                    state = OrderDetailsViewState.State.Success(
                        dateTime = "21 января 19:20",
                        deferredTime = OrderDetailsViewState.HintWithValue(
                            hint = "Время доставки",
                            value = "18:30"
                        ),
                        paymentMethod = "Картой",
                        receiptMethod = "Доставка",
                        address = "улица Чапаева, д 22А, кв 15",
                        comment = "Не забудте привезти еду",
                        status = "Обрабатывается",
                        phoneNumber = "+ 7 (900) 900-90-90",
                        productList = persistentListOf(
                            OrderDetailsViewState.Product(
                                title = "Хот-дог французский с куриной колбаской с супер пупер длинной строкой в названии",
                                price = "99 ₽",
                                count = "× 2",
                                cost = "198 ₽",
                                description = "Необычный лаваш • Добавка 1 • Добавка 2"
                            ),
                            OrderDetailsViewState.Product(
                                title = "Хот-дог французский с куриной колбаской",
                                price = "99 ₽",
                                count = "× 2 + 200 ₽",
                                cost = "198 ₽",
                                description = null
                            )
                        ),
                        deliveryCost = "100 ₽",
                        percentDiscount = "10%",
                        finalCost = "480 ₽",
                        statusColor = AdminTheme.colors.order.notAccepted,
                        saving = false,
                        statusListUI = OrderDetailsViewState.State.Success.StatusListUI(
                            isShown = false,
                            statusList = persistentListOf()
                        )
                    )
                ),
                onAction = {
                }
            )
        }
    }
}
