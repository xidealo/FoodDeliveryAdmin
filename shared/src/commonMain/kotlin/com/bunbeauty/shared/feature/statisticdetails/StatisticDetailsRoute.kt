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
import com.bunbeauty.shared.designsystem.compose.element.image.AdminAsyncImage
import com.bunbeauty.shared.designsystem.compose.element.image.ImageData
import com.bunbeauty.shared.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.bold
import com.bunbeauty.shared.designsystem.compose.theme.medium
import com.bunbeauty.shared.feature.statisticdetails.navigation.StatisticDetailsScreenDestination
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.common_preview_statistic_detail_product_1
import fooddeliveryadmin.shared.generated.resources.common_preview_statistic_detail_product_2
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
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
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
        backgroundColor = AdminTheme.colors.main.surface,
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

@Composable
private fun StatisticDetailsSuccessContent(uiState: StatisticDetailsViewState.State.Success) {
    LazyColumn(
        contentPadding =
            PaddingValues(
                start = AdminTheme.dimensions.screenContentSpace,
                end = AdminTheme.dimensions.screenContentSpace,
                top = AdminTheme.dimensions.screenContentSpace,
                bottom = AdminTheme.dimensions.scrollScreenBottomSpace(),
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            StatisticDetailsInfoCard(uiState = uiState)
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
private fun StatisticDetailsInfoCard(
    modifier: Modifier = Modifier,
    uiState: StatisticDetailsViewState.State.Success,
) {

    Column(
        modifier =
            Modifier
                .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            StatisticDetailsInfoTextColumn(
                modifier = Modifier.weight(1f),
                hint = stringResource(Res.string.msg_statistic_total_count),
                info = uiState.orderCount.toString(),
            )
            StatisticDetailsInfoTextColumn(
                modifier =
                    Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                hint = stringResource(Res.string.msg_statistic_detail_date),
                info = uiState.dateIso,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            StatisticDetailsInfoTextColumn(
                modifier = Modifier.weight(1f),
                hint = stringResource(Res.string.msg_statistic_detail_delivery_order_count_label),
                info = uiState.deliveryOrderCount.toString(),
            )
            StatisticDetailsInfoTextColumn(
                modifier =
                    Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                hint = stringResource(Res.string.msg_statistic_detail_pickup_order_count_label),
                info = uiState.pickupOrderCount.toString(),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            StatisticDetailsInfoTextColumn(
                modifier = Modifier.weight(1f),
                hint = stringResource(Res.string.msg_statistic_detail_orders_total_label),
                info =
                    stringResource(
                        Res.string.msg_statistic_detail_amount_value,
                        uiState.orderProceedsTotal,
                        uiState.currency,
                    ),
            )
            StatisticDetailsInfoTextColumn(
                modifier =
                    Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                hint = stringResource(Res.string.msg_statistic_average_check),
                info =
                    stringResource(
                        Res.string.msg_statistic_detail_average_value,
                        uiState.averageCheck,
                        uiState.currency,
                    ),
            )
        }
        StatisticDetailsInfoTextColumn(
            hint = stringResource(Res.string.msg_statistic_detail_orders_without_delivery),
            info =
                stringResource(
                    Res.string.msg_statistic_detail_amount_value,
                    uiState.orderProceedsProducts,
                    uiState.currency,
                ),
        )
    }
}

@Composable
private fun StatisticDetailsInfoTextColumn(
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

@Suppress("NonSkippableComposable")
@Preview
@Composable
private fun StatisticDetailsScreenLoadingPreview() {
    AdminTheme {
        StatisticDetailsScreen(
            state =
                StatisticDetailsViewState(
                    state = StatisticDetailsViewState.State.Loading,
                ),
            onAction = {},
        )
    }
}

@Suppress("NonSkippableComposable")
@Preview
@Composable
private fun StatisticDetailsScreenErrorPreview() {
    AdminTheme {
        StatisticDetailsScreen(
            state =
                StatisticDetailsViewState(
                    state = StatisticDetailsViewState.State.Error,
                ),
            onAction = {},
        )
    }
}

@Suppress("NonSkippableComposable")
@Preview
@Composable
private fun StatisticDetailsScreenSuccessPreview() {
    AdminTheme {
        StatisticDetailsScreen(
            state =
                StatisticDetailsViewState(
                    state =
                        StatisticDetailsViewState.State.Success(
                            dateIso = "2024-05-01",
                            orderCount = 42,
                            orderProceedsTotal = 125_000,
                            orderProceedsProducts = 98_000,
                            averageCheck = 2976.19,
                            deliveryOrderCount = 28,
                            pickupOrderCount = 14,
                            currency = "$",
                            products =
                                persistentListOf(
                                    StatisticDetailsViewState.State.Success.ProductRow(
                                        menuProductUuid = "preview-1",
                                        name = stringResource(Res.string.common_preview_statistic_detail_product_1),
                                        photoLink = "",
                                        productCount = 12,
                                        proceeds = 45_000,
                                        currency = "$",
                                    ),
                                    StatisticDetailsViewState.State.Success.ProductRow(
                                        menuProductUuid = "preview-2",
                                        name = stringResource(Res.string.common_preview_statistic_detail_product_2),
                                        photoLink = "https://example.com/image.jpg",
                                        productCount = 3,
                                        proceeds = 2100,
                                        currency = "$",
                                    ),
                                ),
                        ),
                ),
            onAction = {},
        )
    }
}
