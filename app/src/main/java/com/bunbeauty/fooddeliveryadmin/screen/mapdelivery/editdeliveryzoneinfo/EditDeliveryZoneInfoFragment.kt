package com.bunbeauty.fooddeliveryadmin.screen.mapdelivery.editdeliveryzoneinfo

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextFieldDefaults.keyboardOptions
import com.bunbeauty.presentation.designsystem.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone.EditDeliveryZoneInfo
import com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone.EditDeliveryZoneInfoViewModel
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditDeliveryZoneInfoFragment :
    SingleStateComposeFragment<EditDeliveryZoneInfo.DataState, EditDeliveryZoneInfo.Action, EditDeliveryZoneInfo.Event>() {
    override val viewModel: EditDeliveryZoneInfoViewModel by viewModel()

    companion object {
        const val SELECT_ZONE_KEY = "SELECT_ZONE_KEY"
        const val UUID_ZONE_KEY = "UUID_ZONE_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(EditDeliveryZoneInfo.Action.InitZone)
    }

    override fun handleEvent(event: EditDeliveryZoneInfo.Event) {
        when (event) {
            is EditDeliveryZoneInfo.Event.Back -> {
                findNavController().navigateUp()
            }

            is EditDeliveryZoneInfo.Event.SaveInfoZoneSuccess -> {
                setFragmentResult(
                    requestKey = SELECT_ZONE_KEY,
                    result = bundleOf(UUID_ZONE_KEY to event.uuid),
                )

                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(
                        R.string.msg_edit_info_delivery_zone_updated,
                        event.zoneName,
                    ),
                )
                findNavController().popBackStack()
            }
        }
    }

    @Composable
    override fun Screen(
        state: EditDeliveryZoneInfo.DataState,
        onAction: (EditDeliveryZoneInfo.Action) -> Unit,
    ) {
        EditInfoDeliveryZoneScreen(
            state = state,
            onAction = onAction,
        )
    }
}

@Composable
private fun EditInfoDeliveryZoneScreen(
    state: EditDeliveryZoneInfo.DataState,
    onAction: (EditDeliveryZoneInfo.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(R.string.title_edit_info_delivery_zone),
        backActionClick = { onAction(EditDeliveryZoneInfo.Action.OnBackClick) },
        backgroundColor = AdminTheme.colors.main.surface,
        actionButton = {
            LoadingButton(
                text = stringResource(R.string.action_order_details_save),
                isLoading = false,
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
                labelText = stringResource(R.string.hint_edit_name_info_delivery_zone),
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
                labelText = stringResource(R.string.hint_edit_min_order_cost_info_delivery_zone),
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
                labelText = stringResource(R.string.hint_edit_normal_cost_info_delivery_zone),
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
                labelText = stringResource(R.string.hint_edit_for_low_cost_info_delivery_zone),
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

@Preview(showSystemUi = true)
@Composable
private fun EditInfoDeliveryZonePreviewScreen() {
    AdminTheme {
        EditInfoDeliveryZoneScreen(
            state =
                EditDeliveryZoneInfo.DataState(
                    state = EditDeliveryZoneInfo.DataState.State.SUCCESS,
                    uuid = "",
                    isLoading = true,
                    nameZona =
                        TextFieldData(
                            value = "Test",
                            isError = false,
                        ),
                    minOrderCost =
                        TextFieldData(
                            value = "10",
                            isError = false,
                        ),
                    normalDeliveryCost =
                        TextFieldData(
                            value = "100",
                            isError = false,
                        ),
                    forLowDeliveryCost =
                        TextFieldData(
                            value = "1000",
                            isError = false,
                        ),
                    hasEditNameError = false,
                ),
            onAction = {},
        )
    }
}
