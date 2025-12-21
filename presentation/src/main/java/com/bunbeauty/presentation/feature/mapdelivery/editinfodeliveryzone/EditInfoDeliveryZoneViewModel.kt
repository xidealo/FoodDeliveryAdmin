package com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone

import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel

class EditInfoDeliveryZoneViewModel :
    BaseStateViewModel<EditInfoDeliveryZone.DataState, EditInfoDeliveryZone.Action, EditInfoDeliveryZone.Event>(
        initState =
            EditInfoDeliveryZone.DataState(
                state = EditInfoDeliveryZone.DataState.State.LOADING,
                isLoading = true,
                nameZona = TextFieldData.empty,
                minOrderCost = TextFieldData.empty,
                normalDeliveryCost = TextFieldData.empty,
                forLowDeliveryCost = TextFieldData.empty,
            ),
    ) {
    override fun reduce(
        action: EditInfoDeliveryZone.Action,
        dataState: EditInfoDeliveryZone.DataState,
    ) {
        when (action) {
            EditInfoDeliveryZone.Action.OnBackClick -> backClick()
            is EditInfoDeliveryZone.Action.EditNameDeliveryZone ->
                editNameZona(
                    nameEditZona = action.nameDeliveryZone,
                )
        }
    }

    private fun backClick() {
        sendEvent { EditInfoDeliveryZone.Event.Back }
    }

    private fun editNameZona(nameEditZona: String) {
        setState {
            copy(
                nameZona =
                    nameZona.copy(
                        value = nameEditZona,
                        isError = false,
                    ),
            )
        }
    }
}
