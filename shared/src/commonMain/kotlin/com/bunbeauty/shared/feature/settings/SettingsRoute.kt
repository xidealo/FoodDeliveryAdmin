package com.bunbeauty.shared.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.domain.model.settings.WorkLoad
import com.bunbeauty.domain.model.settings.WorkType
import com.bunbeauty.shared.designsystem.compose.AdminScaffold
import com.bunbeauty.shared.designsystem.compose.element.bottomsheet.AdminModalBottomSheet
import com.bunbeauty.shared.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.shared.designsystem.compose.element.button.MainButton
import com.bunbeauty.shared.designsystem.compose.element.button.RadioButton
import com.bunbeauty.shared.designsystem.compose.element.button.SecondaryButton
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCard
import com.bunbeauty.shared.designsystem.compose.element.card.AdminCardDefaults.noCornerCardShape
import com.bunbeauty.shared.designsystem.compose.element.card.SwitcherCard
import com.bunbeauty.shared.designsystem.compose.element.topbar.AdminHorizontalDivider
import com.bunbeauty.shared.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.shared.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.shared.designsystem.compose.theme.AdminTheme
import com.bunbeauty.shared.designsystem.compose.theme.bold
import com.bunbeauty.shared.feature.settings.state.SettingsState
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.action_common_cancel
import fooddeliveryadmin.shared.generated.resources.action_edit_addition_save
import fooddeliveryadmin.shared.generated.resources.action_settings_disable
import fooddeliveryadmin.shared.generated.resources.action_work_load_cafe_disable
import fooddeliveryadmin.shared.generated.resources.ic_cellular_average
import fooddeliveryadmin.shared.generated.resources.ic_cellular_high
import fooddeliveryadmin.shared.generated.resources.ic_cellular_low
import fooddeliveryadmin.shared.generated.resources.ic_close_cafe
import fooddeliveryadmin.shared.generated.resources.ic_delivery
import fooddeliveryadmin.shared.generated.resources.ic_delivery_and_pickup
import fooddeliveryadmin.shared.generated.resources.ic_pickup
import fooddeliveryadmin.shared.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.shared.generated.resources.msg_settings_disable_orders
import fooddeliveryadmin.shared.generated.resources.msg_settings_kitchen_appliances
import fooddeliveryadmin.shared.generated.resources.msg_settings_kitchen_appliances_hint
import fooddeliveryadmin.shared.generated.resources.msg_settings_status_accept_orders
import fooddeliveryadmin.shared.generated.resources.msg_settings_status_delivery
import fooddeliveryadmin.shared.generated.resources.msg_settings_status_pickup
import fooddeliveryadmin.shared.generated.resources.msg_settings_status_pickup_delivery
import fooddeliveryadmin.shared.generated.resources.msg_settings_type_work
import fooddeliveryadmin.shared.generated.resources.msg_settings_unlimited_notifications
import fooddeliveryadmin.shared.generated.resources.msg_work_load_average
import fooddeliveryadmin.shared.generated.resources.msg_work_load_high
import fooddeliveryadmin.shared.generated.resources.msg_work_load_low
import fooddeliveryadmin.shared.generated.resources.settings_success_save_message
import fooddeliveryadmin.shared.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.shared.generated.resources.title_settings
import fooddeliveryadmin.shared.generated.resources.title_settings_disable_orders
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsRouteScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    goBack: () -> Unit,
    showInfoMessage: (String, Dp) -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: SettingsState.Action ->
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
        onAction(SettingsState.Action.Init)
    }

    SettingsEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        goBack = goBack,
        showInfoMessage = showInfoMessage,
    )

    SettingsScreen(
        state = viewState.toViewState(),
        onAction = onAction,
        goBack = goBack,
    )
}

@Composable
private fun SettingsEffect(
    effects: List<SettingsState.Event>,
    goBack: () -> Unit,
    showInfoMessage: (String, Dp) -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                SettingsState.Event.GoBackEvent -> {
                    goBack()
                }

                SettingsState.Event.ShowSaveSettingEvent -> {
                    showInfoMessage(getString(Res.string.settings_success_save_message), 0.dp)
                    goBack()
                }

                is SettingsState.Event.ShowErrorMessage -> {
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun SettingsScreen(
    state: SettingsViewState,
    onAction: (SettingsState.Action) -> Unit,
    goBack: () -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_settings),
        backActionClick = {
            onAction(SettingsState.Action.OnBackClicked)
        },
        backgroundColor = AdminTheme.colors.main.surface,
        actionButton = {
            if (state.state is SettingsViewState.State.Success) {
                LoadingButton(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(Res.string.action_edit_addition_save),
                    isLoading = state.state.isLoading,
                    onClick = {
                        onAction(SettingsState.Action.OnSaveSettingsClick)
                    },
                )
            }
        },
    ) {
        when (state.state) {
            SettingsViewState.State.Loading -> {
                LoadingScreen()
            }

            SettingsViewState.State.Error -> {
                ErrorScreen(
                    mainTextId = Res.string.title_common_can_not_load_data,
                    extraTextId = Res.string.msg_common_check_connection_and_retry,
                    onClick = {
                        onAction(SettingsState.Action.Init)
                    },
                )
            }

            is SettingsViewState.State.Success -> {
                SuccessSettingsScreen(
                    state = state.state,
                    onAction = onAction,
                )
            }
        }
    }
}

@Composable
private fun SuccessSettingsScreen(
    state: SettingsViewState.State.Success,
    onAction: (SettingsState.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 72.dp),
    ) {
        WorkTypeScreen(
            state = state,
            onAction = onAction,
        )

        WorkLoadScreen(
            state = state,
            onAction = onAction,
        )

        BottomSheetScreen(
            state = state,
            onAction = onAction,
        )
        SwitcherCard(
            modifier = Modifier.padding(vertical = 8.dp),
            elevated = false,
            text = stringResource(Res.string.msg_settings_unlimited_notifications),
            checked = state.isNotifications,
            onCheckChanged = { isUnlimitedNotifications ->
                onAction(
                    SettingsState.Action.OnNotificationsClicked(
                        isUnlimitedNotifications = isUnlimitedNotifications,
                    ),
                )
            },
        )
        SwitcherCard(
            hint = stringResource(Res.string.msg_settings_kitchen_appliances_hint),
            modifier = Modifier.padding(vertical = 8.dp),
            elevated = false,
            text = stringResource(Res.string.msg_settings_kitchen_appliances),
            checked = state.isAppliances,
            onCheckChanged = { isKitchenAppliances ->
                onAction(
                    SettingsState.Action.OnAppliancesClicked(
                        isKitchenAppliances = isKitchenAppliances,
                    ),
                )
            },
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun BottomSheetScreen(
    state: SettingsViewState.State.Success,
    onAction: (SettingsState.Action) -> Unit,
) {
    AdminModalBottomSheet(
        title = stringResource(state.acceptOrdersConfirmation.titleStringId),
        isShown = state.acceptOrdersConfirmation.isShown,
        onDismissRequest = {
            onAction(SettingsState.Action.CancelAcceptOrders)
        },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(state.acceptOrdersConfirmation.descriptionStringId),
                style = AdminTheme.typography.bodyMedium,
                color = AdminTheme.colors.main.onSurface,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            MainButton(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(state.acceptOrdersConfirmation.buttonStringId),
                onClick = {
                    onAction(SettingsState.Action.ConfirmNotAcceptOrders)
                },
            )
            SecondaryButton(
                modifier = Modifier.padding(top = 8.dp),
                textStringId = Res.string.action_common_cancel,
                onClick = {
                    onAction(SettingsState.Action.CancelAcceptOrders)
                },
            )
        }
    }
}

@Composable
private fun WorkTypeScreen(
    state: SettingsViewState.State.Success,
    onAction: (SettingsState.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(bottom = 16.dp)) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(Res.string.msg_settings_type_work),
            style = AdminTheme.typography.titleMedium.bold,
        )
        SettingsTypeRow(
            iconId = Res.drawable.ic_delivery,
            textStringId = Res.string.msg_settings_status_delivery,
            isSelected = state.workType == WorkType.DELIVERY,
            onClick = {
                onAction(
                    SettingsState.Action.OnSelectStatusClicked(
                        WorkType.DELIVERY,
                    ),
                )
            },
        )
        AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        SettingsTypeRow(
            iconId = Res.drawable.ic_pickup,
            textStringId = Res.string.msg_settings_status_pickup,
            isSelected = state.workType == WorkType.PICKUP,
            onClick = {
                onAction(
                    SettingsState.Action.OnSelectStatusClicked(
                        WorkType.PICKUP,
                    ),
                )
            },
        )
        AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        SettingsTypeRow(
            iconId = Res.drawable.ic_delivery_and_pickup,
            textStringId = Res.string.msg_settings_status_pickup_delivery,
            isSelected = state.workType == WorkType.DELIVERY_AND_PICKUP,
            onClick = {
                onAction(
                    SettingsState.Action.OnSelectStatusClicked(
                        WorkType.DELIVERY_AND_PICKUP,
                    ),
                )
            },
        )
        AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        SettingsTypeRow(
            iconId = Res.drawable.ic_close_cafe,
            textStringId = Res.string.msg_settings_status_accept_orders,
            isSelected = state.workType == WorkType.CLOSED,
            onClick = {
                onAction(
                    SettingsState.Action.OnSelectStatusClicked(
                        WorkType.CLOSED,
                    ),
                )
            },
        )
    }
}

@Composable
private fun WorkLoadScreen(
    state: SettingsViewState.State.Success,
    onAction: (SettingsState.Action) -> Unit,
) {
    Text(
        modifier = Modifier.padding(horizontal = 16.dp),
        text = stringResource(Res.string.action_work_load_cafe_disable),
        style = AdminTheme.typography.titleMedium.bold,
    )
    Column(modifier = Modifier.padding(top = 8.dp)) {
        SettingsTypeRow(
            iconId = Res.drawable.ic_cellular_low,
            textStringId = Res.string.msg_work_load_low,
            isSelected = state.workLoad == WorkLoad.LOW,
            onClick = {
                onAction(
                    SettingsState.Action.OnSelectWorkLoadClicked(
                        WorkLoad.LOW,
                    ),
                )
            },
        )
        AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        SettingsTypeRow(
            iconId = Res.drawable.ic_cellular_average,
            textStringId = Res.string.msg_work_load_average,
            isSelected = state.workLoad == WorkLoad.AVERAGE,
            onClick = {
                onAction(
                    SettingsState.Action.OnSelectWorkLoadClicked(
                        WorkLoad.AVERAGE,
                    ),
                )
            },
        )
        AdminHorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        SettingsTypeRow(
            iconId = Res.drawable.ic_cellular_high,
            textStringId = Res.string.msg_work_load_high,
            isSelected = state.workLoad == WorkLoad.HIGH,
            onClick = {
                onAction(
                    SettingsState.Action.OnSelectWorkLoadClicked(
                        WorkLoad.HIGH,
                    ),
                )
            },
        )
    }
}

@Composable
private fun SettingsTypeRow(
    iconId: DrawableResource,
    textStringId: StringResource,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AdminCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        elevated = false,
        shape = noCornerCardShape,
    ) {
        Row(
            modifier =
                modifier
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier =
                    Modifier
                        .padding(start = 8.dp)
                        .size(24.dp),
                painter = painterResource(iconId),
                tint = AdminTheme.colors.main.onSurfaceVariant,
                contentDescription = null,
            )
            Text(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(start = 16.dp),
                text = stringResource(textStringId),
                style = AdminTheme.typography.bodyLarge,
                color = AdminTheme.colors.main.onSurface,
            )
            RadioButton(
                selected = isSelected,
                onClick = onClick,
            )
        }
    }
}

@Preview()
@Composable
private fun SettingsScreenPreview() {
    AdminTheme {
        SettingsScreen(
            state =
                SettingsViewState(
                    state =
                        SettingsViewState.State.Success(
                            isNotifications = true,
                            isAppliances = true,
                            workType = WorkType.DELIVERY_AND_PICKUP,
                            acceptOrdersConfirmation =
                                SettingsViewState.AcceptOrdersConfirmation(
                                    isShown = false,
                                    titleStringId = Res.string.title_settings_disable_orders,
                                    descriptionStringId = Res.string.msg_settings_disable_orders,
                                    buttonStringId = Res.string.action_settings_disable,
                                ),
                            isLoading = false,
                            workLoad = WorkLoad.LOW,
                        ),
                ),
            onAction = {},
            goBack = {},
        )
    }
}
