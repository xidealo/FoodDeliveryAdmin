package com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone

import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface EditDeliveryZoneInfo {
    data class DataState(
        val state: State,
        val uuid: String,
        val isLoading: Boolean,
        val hasEditNameError: Boolean,
        val nameZona: TextFieldData,
        val minOrderCost: TextFieldData,
        val normalDeliveryCost: TextFieldData,
        val forLowDeliveryCost: TextFieldData,
    ) : BaseViewDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR,
        }

        enum class NameStateError {
            EMPTY_NAME,
        }
    }

    sealed interface Action : BaseAction {
        data object OnBackClick : Action

        data object InitZone : Action

        data object SaveDeliveryZone : Action

        data class EditNameDeliveryZone(
            val nameDeliveryZone: String,
        ) : Action

        data class EditMinOrderCostDeliveryZone(
            val minOrderCost: String,
        ) : Action

        data class EditNormalDeliveryCostDeliveryZone(
            val normalDeliveryCost: String,
        ) : Action

        data class EditForLowDeliveryCostDeliveryZone(
            val forLowDeliveryCost: String,
        ) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event

        data class SaveInfoZoneSuccess(
            val zoneName: String,
            val uuid: String,
        ) : Event

        // data object SaveInfoZone : Event
    }
}
