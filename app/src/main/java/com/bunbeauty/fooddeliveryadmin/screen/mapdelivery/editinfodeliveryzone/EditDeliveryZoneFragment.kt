package com.bunbeauty.fooddeliveryadmin.screen.mapdelivery.editinfodeliveryzone

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.compose.AdminScaffold
import com.bunbeauty.fooddeliveryadmin.compose.element.textfield.AdminTextField
import com.bunbeauty.fooddeliveryadmin.coreui.SingleStateComposeFragment
import com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone.EditInfoDeliveryZone
import com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone.EditInfoDeliveryZoneViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditDeliveryZoneFragment :
    SingleStateComposeFragment<EditInfoDeliveryZone.DataState, EditInfoDeliveryZone.Action, EditInfoDeliveryZone.Event>() {
    override val viewModel: EditInfoDeliveryZoneViewModel by viewModel()

    override fun handleEvent(event: EditInfoDeliveryZone.Event) {
        when (event) {
            is EditInfoDeliveryZone.Event.Back -> {
                findNavController().navigateUp()
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
    ) {
        Column {
            AdminTextField(
                modifier = Modifier.padding(16.dp),
                labelText = stringResource(R.string.hint_edit_addition_group_name),
                value = state.nameZona.value,
                onValueChange = { name ->
                    onAction(
                        EditInfoDeliveryZone.Action.EditNameDeliveryZone(nameDeliveryZone = name),
                    )
                },
                isError = state.nameZona.isError,
                enabled = !state.isLoading,
            )
            AdminTextField(
                modifier = Modifier.padding(16.dp),
                labelText = stringResource(R.string.hint_edit_addition_group_name),
                value = state.nameZona.value,
                onValueChange = { name ->
                    onAction(
                        EditInfoDeliveryZone.Action.EditNameDeliveryZone(nameDeliveryZone = name),
                    )
                },
                isError = state.nameZona.isError,
                enabled = !state.isLoading,
            )
            AdminTextField(
                modifier = Modifier.padding(16.dp),
                labelText = stringResource(R.string.hint_edit_addition_group_name),
                value = state.nameZona.value,
                onValueChange = { name ->
                    onAction(
                        EditInfoDeliveryZone.Action.EditNameDeliveryZone(nameDeliveryZone = name),
                    )
                },
                isError = state.nameZona.isError,
                enabled = !state.isLoading,
            )
            AdminTextField(
                modifier = Modifier.padding(16.dp),
                labelText = stringResource(R.string.hint_edit_addition_group_name),
                value = state.nameZona.value,
                onValueChange = { name ->
                    onAction(
                        EditInfoDeliveryZone.Action.EditNameDeliveryZone(nameDeliveryZone = name),
                    )
                },
                isError = state.nameZona.isError,
                enabled = !state.isLoading,
            )
        }
    }
}
