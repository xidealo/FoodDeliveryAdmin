package com.bunbeauty.shared.feature.statisticdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCard
import com.bunbeauty.shared.designsystem.compose.element.card.TextWithHintCard
import com.bunbeauty.shared.designsystem.compose.element.image.AdminAsyncImage
import com.bunbeauty.shared.designsystem.compose.element.image.ImageData
import com.bunbeauty.shared.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.bold
import com.bunbeauty.shared.feature.statisticdetails.navigation.StatisticDetailsScreenDestination
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.default_product
import fooddeliveryadmin.shared.generated.resources.description_statistic_product_photo
import fooddeliveryadmin.shared.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.shared.generated.resources.msg_statistic_average_check
import fooddeliveryadmin.shared.generated.resources.msg_statistic_detail_amount_value
import fooddeliveryadmin.shared.generated.resources.msg_statistic_detail_average_value
import fooddeliveryadmin.shared.generated.resources.msg_statistic_detail_date
import fooddeliveryadmin.shared.generated.resources.msg_statistic_detail_delivery_order_count_label
import fooddeliveryadmin.shared.generated.resources.msg_statistic_detail_orders_total_label
import fooddeliveryadmin.shared.generated.resources.msg_statistic_detail_orders_without_delivery
import fooddeliveryadmin.shared.generated.resources.msg_statistic_detail_pickup_order_count_label
import fooddeliveryadmin.shared.generated.resources.msg_statistic_detail_product_sold
import fooddeliveryadmin.shared.generated.resources.msg_statistic_detail_product_sum
import fooddeliveryadmin.shared.generated.resources.msg_statistic_total_count
import fooddeliveryadmin.shared.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.shared.generated.resources.title_statistic_details
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StatisticDetailsRouteScreen(
    viewModel: StatisticDetailsViewModel = koinViewModel(),
    goBack: () -> Unit,
    backStackEntry: NavBackStackEntry,
) {
    val route = backStackEntry.toRoute<StatisticDetailsScreenDestination>()
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: StatisticDetails.Action ->
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

    LaunchedEffect(route.date) {
        onAction(
            StatisticDetails.Action.Init(
                dateIso = route.date,
            ),
        )
    }

    StatisticDetailsEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        goBack = goBack,
    )

    StatisticDetailsScreen(
        state = viewState.toViewState(),
        onAction = onAction,
    )
}

@Composable
private fun StatisticDetailsEffect(
    effects: List<StatisticDetails.Event>,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                StatisticDetails.Event.GoBack -> {
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun StatisticDetailsScreen(
    state: StatisticDetailsViewState,
    onAction: (StatisticDetails.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_statistic_details),
        backActionClick = {
            onAction(StatisticDetails.Action.SelectGoBackClick)
        },
    ) {
        when (val uiState = state.state) {
            StatisticDetailsViewState.State.Loading -> LoadingScreen()
            StatisticDetailsViewState.State.Error ->
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(StatisticDetails.Action.Retry)
                    },
                )

            is StatisticDetailsViewState.State.Success ->
                StatisticDetailsSuccessContent(
                    uiState = uiState,
                )
        }
    }
}

@Suppress("NonSkippableComposable")
@Composable
private fun StatisticDetailsSuccessContent(uiState: StatisticDetailsViewState.State.Success) {
    LazyColumn(
        contentPadding =
            PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace(),
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            StatisticSummaryTwoColumnRow(
                startHint = Res.string.msg_statistic_total_count,
                startLabel = uiState.orderCount.toString(),
                endHint = Res.string.msg_statistic_detail_date,
                endLabel = uiState.dateIso,
            )
        }
        item {
            StatisticSummaryTwoColumnRow(
                startHint = Res.string.msg_statistic_detail_delivery_order_count_label,
                startLabel = uiState.deliveryOrderCount.toString(),
                endHint = Res.string.msg_statistic_detail_pickup_order_count_label,
                endLabel = uiState.pickupOrderCount.toString(),
            )
        }
        item {
            StatisticSummaryTwoColumnRow(
                startHint = Res.string.msg_statistic_detail_orders_total_label,
                startLabel =
                    stringResource(
                        Res.string.msg_statistic_detail_amount_value,
                        uiState.orderProceedsTotal,
                        uiState.currency,
                    ),
                endHint = Res.string.msg_statistic_average_check,
                endLabel =
                    stringResource(
                        Res.string.msg_statistic_detail_average_value,
                        uiState.averageCheck,
                        uiState.currency,
                    ),
            )
        }
        item {
            TextWithHintCard(
                modifier = Modifier.fillMaxWidth(),
                hintStringId = Res.string.msg_statistic_detail_orders_without_delivery,
                label =
                    stringResource(
                        Res.string.msg_statistic_detail_amount_value,
                        uiState.orderProceedsProducts,
                        uiState.currency,
                    ),
            )
        }
        items(
            items = uiState.products,
            key = { product ->
                product.menuProductUuid
            },
        ) { product ->
            StatisticDetailsProductRow(product = product)
        }
    }
}

@Composable
private fun StatisticSummaryTwoColumnRow(
    startHint: StringResource,
    startLabel: String,
    endHint: StringResource,
    endLabel: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TextWithHintCard(
            modifier = Modifier.weight(1f),
            hintStringId = startHint,
            label = startLabel,
        )
        TextWithHintCard(
            modifier = Modifier.weight(1f),
            hintStringId = endHint,
            label = endLabel,
        )
    }
}

@Suppress("NonSkippableComposable")
@Composable
private fun StatisticDetailsProductRow(product: StatisticDetailsViewState.State.Success.ProductRow) {
    AdminCard(
        modifier = Modifier.fillMaxWidth(),
        clickable = false,
    ) {
        Row(
            modifier =
                Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp,
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val imageData =
                if (product.photoLink.isBlank()) {
                    ImageData.LocalId(Res.drawable.default_product)
                } else {
                    ImageData.HttpUrl(product.photoLink)
                }
            AdminAsyncImage(
                contentDescription = Res.string.description_statistic_product_photo,
                imageData = imageData,
                modifier = Modifier.size(56.dp),
            )
            Column(
                modifier =
                    Modifier
                        .padding(start = 12.dp)
                        .weight(1f),
            ) {
                Text(
                    text = product.name,
                    style = AdminTheme.typography.titleSmall,
                    color = AdminTheme.colors.main.onSurface,
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text =
                        stringResource(
                            Res.string.msg_statistic_detail_product_sold,
                            product.productCount,
                        ),
                    style = AdminTheme.typography.bodySmall,
                    color = AdminTheme.colors.main.onSurface,
                )
                Text(
                    modifier = Modifier.padding(top = 2.dp),
                    text =
                        stringResource(
                            Res.string.msg_statistic_detail_product_sum,
                            product.proceeds,
                            product.currency,
                        ),
                    style = AdminTheme.typography.bodySmall.bold,
                    color = AdminTheme.colors.main.onSurface,
                )
            }
        }
    }
}
