package com.bunbeauty.shared.feature.statisticuserdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCard
import com.bunbeauty.shared.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.medium
import com.bunbeauty.shared.feature.statisticuserdetails.navigation.StatisticUserDetailsScreenDestination
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_average_check
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_delivery_order_count
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_first_order_date
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_last_order_date
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_order_count
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_phone_number
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_pickup_order_count
import fooddeliveryadmin.shared.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.shared.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.shared.generated.resources.title_statistic_user_details
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StatisticUserDetailsRouteScreen(
    backStackEntry: NavBackStackEntry,
    viewModel: StatisticUserDetailsViewModel = koinViewModel(),
    goBack: () -> Unit,
) {
    val route = backStackEntry.toRoute<StatisticUserDetailsScreenDestination>()
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { action: StatisticUserDetails.Action ->
                viewModel.onAction(action)
            }
        }

    val effects by viewModel.events.collectAsStateWithLifecycle()
    val consumeEffects =
        remember {
            {
                viewModel.consumeEvents(effects)
            }
        }

    LaunchedEffect(route.userUuid) {
        onAction(StatisticUserDetails.Action.Init(userUuid = route.userUuid))
    }

    StatisticUserDetailsEffect(
        effects = effects,
        goBack = goBack,
        consumeEffects = consumeEffects,
    )

    StatisticUserDetailsScreen(
        state = viewState.toViewState(),
        onAction = onAction,
    )
}

@Composable
private fun StatisticUserDetailsEffect(
    effects: List<StatisticUserDetails.Event>,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                StatisticUserDetails.Event.GoBack -> goBack()
            }
        }
        consumeEffects()
    }
}

@Composable
private fun StatisticUserDetailsScreen(
    state: StatisticUserDetailsViewState,
    onAction: (StatisticUserDetails.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_statistic_user_details),
        backActionClick = {
            onAction(StatisticUserDetails.Action.BackClick)
        },
        backgroundColor = AdminTheme.colors.main.surface,
    ) {
        when (val currentState = state.state) {
            StatisticUserDetailsViewState.State.Loading -> LoadingScreen()

            StatisticUserDetailsViewState.State.Error ->
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(StatisticUserDetails.Action.Retry)
                    },
                )

            is StatisticUserDetailsViewState.State.Success ->
                StatisticUserDetailsSuccessContent(state = currentState)
        }
    }
}

@Composable
private fun StatisticUserDetailsSuccessContent(state: StatisticUserDetailsViewState.State.Success) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
    ) {
        AdminCard(
            modifier = Modifier.fillMaxWidth(),
            clickable = false,
            elevated = false,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    StatisticUserDetailsInfoColumn(
                        modifier = Modifier.weight(1f),
                        hint = stringResource(Res.string.hint_statistic_user_first_order_date),
                        info = state.firstOrderDate,
                    )
                    StatisticUserDetailsInfoColumn(
                        modifier =
                            Modifier
                                .padding(start = 16.dp)
                                .weight(1f),
                        hint = stringResource(Res.string.hint_statistic_user_last_order_date),
                        info = state.lastOrderDate,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    StatisticUserDetailsInfoColumn(
                        modifier = Modifier.weight(1f),
                        hint = stringResource(Res.string.hint_statistic_user_delivery_order_count),
                        info = state.deliveryOrderCount,
                    )
                    StatisticUserDetailsInfoColumn(
                        modifier =
                            Modifier
                                .padding(start = 16.dp)
                                .weight(1f),
                        hint = stringResource(Res.string.hint_statistic_user_pickup_order_count),
                        info = state.pickupOrderCount,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    StatisticUserDetailsInfoColumn(
                        modifier = Modifier.weight(1f),
                        hint = stringResource(Res.string.hint_statistic_user_average_check),
                        info = state.averageCheck,
                    )
                    StatisticUserDetailsInfoColumn(
                        modifier =
                            Modifier
                                .padding(start = 16.dp)
                                .weight(1f),
                        hint = stringResource(Res.string.hint_statistic_user_order_count),
                        info = state.orderCount,
                    )
                }
                StatisticUserDetailsInfoColumn(
                    hint = stringResource(Res.string.hint_statistic_user_phone_number),
                    info = state.phoneNumber,
                )
            }
        }
    }
}

@Composable
private fun StatisticUserDetailsInfoColumn(
    hint: String,
    info: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = hint,
            style = AdminTheme.typography.labelSmall.medium,
            color = AdminTheme.colors.main.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = info,
            style = AdminTheme.typography.bodyMedium,
            color = AdminTheme.colors.main.onSurface,
        )
    }
}

@Suppress("NonSkippableComposable")
@Preview
@Composable
private fun StatisticUserDetailsScreenPreview() {
    AdminTheme {
        StatisticUserDetailsScreen(
            state =
                StatisticUserDetailsViewState(
                    state =
                        StatisticUserDetailsViewState.State.Success(
                            phoneNumber = "+7 996 922 41 86",
                            firstOrderDate = "2026-05-10",
                            lastOrderDate = "2026-05-10",
                            deliveryOrderCount = "100",
                            pickupOrderCount = "75",
                            averageCheck = "500 ₽",
                            orderCount = "25",
                        ),
                ),
            onAction = {},
        )
    }
}
