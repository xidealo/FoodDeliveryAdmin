package com.bunbeauty.presentation.feature.mapdelivery.editinfodeliveryzone

import com.bunbeauty.presentation.feature.menulist.common.TextFieldData
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface EditInfoDeliveryZone {
    data class DataState(
        val state: State,
        val isLoading: Boolean,
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

        data class EditNameDeliveryZone(
            val nameDeliveryZone: String,
        ) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
    }
}
