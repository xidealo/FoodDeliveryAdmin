package com.bunbeauty.fooddeliveryadmin.screen.mapdelivery.editinfodeliveryzone

import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.button.LoadingButton
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextField
import com.bunbeauty.fooddeliveryadmin.compose.theme.AdminTheme
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.fooddeliveryadmin.main.MessageHost
import com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone.EditInfoDeliveryZone
import com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone.EditInfoDeliveryZoneViewModel
import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import org.koin.androidx.viewmodel.ext.android.viewModel

// Переименовать EditDeliveryZoneInfo
class EditDeliveryZoneFragment :
    SingleStateComposeFragment<EditInfoDeliveryZone.DataState, EditInfoDeliveryZone.Action, EditInfoDeliveryZone.Event>() {
    override val viewModel: EditInfoDeliveryZoneViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAction(EditInfoDeliveryZone.Action.InitZone)
    }

    override fun handleEvent(event: EditInfoDeliveryZone.Event) {
        when (event) {
            is EditInfoDeliveryZone.Event.Back -> {
                findNavController().navigateUp()
            }

            is EditInfoDeliveryZone.Event.SaveInfoZoneSuccess -> {

                /*setFragmentResult(
                    requestKey = SELECT_ADDITION_LIST_KEY,
                    положить ююид зоны которую обновил
                    result = bundleOf(ADDITION_LIST_KEY to event.additionUuidList),
                )*/

                (activity as? MessageHost)?.showInfoMessage(
                    resources.getString(R.string.msg_edit_info_delivery_zone_updated, event.zoneName),
                )
                findNavController().popBackStack()
            }
        }
    }

    @Composable
    override fun Screen(
        state: EditInfoDeliveryZone.DataState,
        onAction: (EditInfoDeliveryZone.Action) -> Unit,
    ) {
        EditInfoDeliveryZoneScreen(
            state = state,
            onAction = onAction,
        )
    }
}

@Composable
private fun EditInfoDeliveryZoneScreen(
    state: EditInfoDeliveryZone.DataState,
    onAction: (EditInfoDeliveryZone.Action) -> Unit,
) {
    AdminScaffold(
        title = stringResource(R.string.title_edit_info_delivery_zone),
        backActionClick = { onAction(EditInfoDeliveryZone.Action.OnBackClick) },
        backgroundColor = AdminTheme.colors.main.surface,
        actionButton = {
            LoadingButton(
                text = stringResource(R.string.action_order_details_save),
                isLoading = false,
                onClick = { onAction(EditInfoDeliveryZone.Action.SaveDeliveryZone) },
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
                modifier = Modifier.padding(top = 16.dp),
                labelText = stringResource(R.string.hint_edit_name_info_delivery_zone),
                value = state.nameZona.value,
                onValueChange = { name ->
                    onAction(
                        EditInfoDeliveryZone.Action.EditNameDeliveryZone(nameDeliveryZone = name),
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
                        EditInfoDeliveryZone.Action.EditMinOrderCostDeliveryZone(minOrderCost = minOrderCost),
                    )
                },
                isError = state.nameZona.isError,
                enabled = !state.isLoading,
            )
            AdminTextField(
                modifier = Modifier.padding(top = 16.dp),
                labelText = stringResource(R.string.hint_edit_normal_cost_info_delivery_zone),
                value = state.normalDeliveryCost.value,
                onValueChange = { normalCost ->
                    onAction(
                        EditInfoDeliveryZone.Action.EditNormalDeliveryCostDeliveryZone(normalDeliveryCost = normalCost),
                    )
                },
                isError = state.nameZona.isError,
                enabled = !state.isLoading,
            )
            AdminTextField(
                modifier = Modifier.padding(top = 16.dp),
                labelText = stringResource(R.string.hint_edit_for_low_cost_info_delivery_zone),
                value = state.forLowDeliveryCost.value,
                onValueChange = { freeCost ->
                    onAction(
                        EditInfoDeliveryZone.Action.EditForLowDeliveryCostDeliveryZone(forLowDeliveryCost = freeCost),
                    )
                },
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
                EditInfoDeliveryZone.DataState(
                    state = EditInfoDeliveryZone.DataState.State.SUCCESS,
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
