package com.bunbeauty.shared.feature.statisticuserdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.bottomBarPadding
import com.bunbeauty.shared.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.shared.designsystem.compose.element.button.MainButton
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCard
import com.bunbeauty.shared.designsystem.compose.element.card.SwitcherCard
import com.bunbeauty.shared.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.medium
import com.bunbeauty.shared.feature.statisticuserdetails.navigation.StatisticUserDetailsScreenDestination
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_statistic_user_save
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_average_check
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_delivery_order_count
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_first_order_date
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_last_order_date
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_order_count
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_phone_number
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_pickup_order_count
import fooddeliveryadmin.shared.generated.resources.hint_statistic_user_problematic
import fooddeliveryadmin.shared.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.shared.generated.resources.msg_statistic_user_saved
import fooddeliveryadmin.shared.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.shared.generated.resources.title_statistic_user_details
import fooddeliveryadmin.shared.generated.resources.title_statistic_user_problematic
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StatisticUserDetailsRouteScreen(
    backStackEntry: NavBackStackEntry,
    viewModel: StatisticUserDetailsViewModel = koinViewModel(),
    goBack: () -> Unit,
    showInfoMessage: (String, Dp) -> Unit,
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
        showInfoMessage = showInfoMessage,
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
    showInfoMessage: (String, Dp) -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                StatisticUserDetails.Event.GoBack -> goBack()
                StatisticUserDetails.Event.ShowSavedMessage -> {
                    showInfoMessage(
                        getString(Res.string.msg_statistic_user_saved),
                        0.dp,
                    )
                }
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
    val successState = state.state as? StatisticUserDetailsViewState.State.Success

    AdminScaffold(
        title = stringResource(Res.string.title_statistic_user_details),
        backActionClick = {
            onAction(StatisticUserDetails.Action.BackClick)
        },
        backgroundColor = AdminTheme.colors.main.surface,
        actionButton = {
            successState?.let { success ->
                if (success.saving) {
                    LoadingButton(
                        modifier =
                            Modifier
                                .padding(horizontal = 16.dp)
                                .bottomBarPadding(),
                        text = stringResource(Res.string.action_statistic_user_save),
                        isLoading = true,
                        onClick = {},
                    )
                } else {
                    MainButton(
                        modifier =
                            Modifier
                                .padding(horizontal = 16.dp)
                                .bottomBarPadding(),
                        text = stringResource(Res.string.action_statistic_user_save),
                        isEnabled = success.hasChanges,
                        elevated = false,
                        onClick = {
                            onAction(StatisticUserDetails.Action.OnSaveClick)
                        },
                    )
                }
            }
        },
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
                StatisticUserDetailsSuccessContent(
                    state = currentState,
                    onAction = onAction,
                )
        }
    }
}

@Composable
private fun StatisticUserDetailsSuccessContent(
    state: StatisticUserDetailsViewState.State.Success,
    onAction: (StatisticUserDetails.Action) -> Unit,
) {
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

        SwitcherCard(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            text = stringResource(Res.string.title_statistic_user_problematic),
            hint = stringResource(Res.string.hint_statistic_user_problematic),
            checked = state.isProblematic,
            elevated = false,
            enabled = !state.saving,
            onCheckChanged = { isProblematic ->
                onAction(StatisticUserDetails.Action.OnProblematicChecked(isProblematic))
            },
        )

        Spacer(modifier = Modifier.height(AdminTheme.dimensions.scrollScreenBottomSpace()))
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
                            isProblematic = true,
                            hasChanges = true,
                            saving = false,
                        ),
                ),
            onAction = {},
        )
    }
}
