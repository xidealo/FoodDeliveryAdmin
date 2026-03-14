package com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bunbeauty.presentation.designsystem.compose.AdminScaffold
import com.bunbeauty.presentation.designsystem.compose.element.button.LoadingButton
import com.bunbeauty.presentation.designsystem.compose.element.textfield.AdminTextField
import com.bunbeauty.presentation.designsystem.compose.element.textfield.AdminTextFieldDefaults.keyboardOptions
import com.bunbeauty.presentation.designsystem.compose.screen.ErrorScreen
import com.bunbeauty.presentation.designsystem.compose.screen.LoadingScreen
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.action_order_details_save
import fooddeliveryadmin.presentation.generated.resources.hint_edit_for_low_cost_info_delivery_zone
import fooddeliveryadmin.presentation.generated.resources.hint_edit_min_order_cost_info_delivery_zone
import fooddeliveryadmin.presentation.generated.resources.hint_edit_name_info_delivery_zone
import fooddeliveryadmin.presentation.generated.resources.hint_edit_normal_cost_info_delivery_zone
import fooddeliveryadmin.presentation.generated.resources.msg_common_check_connection_and_retry
import fooddeliveryadmin.presentation.generated.resources.msg_edit_info_delivery_zone_updated
import fooddeliveryadmin.presentation.generated.resources.title_common_can_not_load_data
import fooddeliveryadmin.presentation.generated.resources.title_edit_info_delivery_zone
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditDeliveryZoneInfoRouteScreen(
    viewModel: EditDeliveryZoneInfoViewModel = koinViewModel(),
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val onAction =
        remember {
            { event: EditDeliveryZoneInfo.Action ->
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
        onAction(EditDeliveryZoneInfo.Action.InitZone)
    }

    EditDeliveryZoneInfoEffect(
        effects = effects,
        consumeEffects = consumeEffects,
        showInfoMessage = showInfoMessage,
        goBack = goBack,
    )

    EditDeliveryZoneInfoScreen(
        state = viewState,
        onAction = onAction,
    )
}

@Composable
private fun EditDeliveryZoneInfoEffect(
    effects: List<EditDeliveryZoneInfo.Event>,
    showInfoMessage: (String, Int) -> Unit,
    goBack: () -> Unit,
    consumeEffects: () -> Unit,
) {
    LaunchedEffect(effects) {
        effects.forEach { effect ->
            when (effect) {
                EditDeliveryZoneInfo.Event.Back -> {
                    goBack()
                }

                is EditDeliveryZoneInfo.Event.SaveInfoZoneSuccess -> {
                    showInfoMessage(
                        getString(
                            Res.string.msg_edit_info_delivery_zone_updated,
                            effect.zoneName,
                        ),
                        0,
                    )
                    goBack()
                }
            }
        }
        consumeEffects()
    }
}

@Composable
private fun EditDeliveryZoneInfoScreen(
    state: EditDeliveryZoneInfo.DataState,
    onAction: (EditDeliveryZoneInfo.Action) -> Unit,
) {
    when (state.state) {
        EditDeliveryZoneInfo.DataState.State.LOADING -> {
            LoadingScreen()
        }

        EditDeliveryZoneInfo.DataState.State.ERROR -> {
            ErrorScreen(
                mainTextId = Res.string.title_common_can_not_load_data,
                extraTextId = Res.string.msg_common_check_connection_and_retry,
                onClick = {
                    onAction(EditDeliveryZoneInfo.Action.InitZone)
                },
            )
        }

        EditDeliveryZoneInfo.DataState.State.SUCCESS -> {
            EditDeliveryZoneInfoSuccessScreen(
                state = state,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun EditDeliveryZoneInfoSuccessScreen(
    state: EditDeliveryZoneInfo.DataState,
    onAction: (EditDeliveryZoneInfo.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(Res.string.title_edit_info_delivery_zone),
        backActionClick = { onAction(EditDeliveryZoneInfo.Action.OnBackClick) },
        backgroundColor = AdminTheme.colors.main.surface,
        actionButton = {
            LoadingButton(
                text = stringResource(Res.string.action_order_details_save),
                isLoading = state.isLoading,
                onClick = { onAction(EditDeliveryZoneInfo.Action.SaveDeliveryZone) },
                modifier =
                    Modifier
                        .padding(horizontal = AdminTheme.dimensions.mediumSpace),
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
        ) {
            AdminTextField(
                labelText = stringResource(Res.string.hint_edit_name_info_delivery_zone),
                value = state.nameZona.value,
                onValueChange = { name ->
                    onAction(
                        EditDeliveryZoneInfo.Action.EditNameDeliveryZone(nameDeliveryZone = name),
                    )
                },
                isError = state.hasEditNameError,
                enabled = !state.isLoading,
            )
            AdminTextField(
                modifier = Modifier.padding(top = 16.dp),
                labelText = stringResource(Res.string.hint_edit_min_order_cost_info_delivery_zone),
                value = state.minOrderCost.value,
                onValueChange = { minOrderCost ->
                    onAction(
                        EditDeliveryZoneInfo.Action.EditMinOrderCostDeliveryZone(minOrderCost = minOrderCost),
                    )
                },
                keyboardOptions =
                    keyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                isError = state.nameZona.isError,
                enabled = !state.isLoading,
            )
            AdminTextField(
                modifier = Modifier.padding(top = 16.dp),
                labelText = stringResource(Res.string.hint_edit_normal_cost_info_delivery_zone),
                value = state.normalDeliveryCost.value,
                onValueChange = { normalCost ->
                    onAction(
                        EditDeliveryZoneInfo.Action.EditNormalDeliveryCostDeliveryZone(
                            normalDeliveryCost = normalCost,
                        ),
                    )
                },
                keyboardOptions =
                    keyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                isError = state.nameZona.isError,
                enabled = !state.isLoading,
            )
            AdminTextField(
                modifier = Modifier.padding(top = 16.dp),
                labelText = stringResource(Res.string.hint_edit_for_low_cost_info_delivery_zone),
                value = state.forLowDeliveryCost.value,
                onValueChange = { freeCost ->
                    onAction(
                        EditDeliveryZoneInfo.Action.EditForLowDeliveryCostDeliveryZone(
                            forLowDeliveryCost = freeCost,
                        ),
                    )
                },
                keyboardOptions =
                    keyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                isError = state.nameZona.isError,
                enabled = !state.isLoading,
            )
        }
    }
}
