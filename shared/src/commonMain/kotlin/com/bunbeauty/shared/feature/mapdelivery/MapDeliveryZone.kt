package com.bunbeauty.shared.feature.mapdelivery

import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseEvent
import com.bunbeauty.shared.viewmodel.base.BaseViewDataState
import org.maplibre.spatialk.geojson.Position

interface MapDeliveryZone {
    data class DataState(
        val state: State,
        val listDeliveryAreaZone: List<ZoneData>,
        val positionCafe: Position?,
        val selectedZoneData: ZoneData? = null,
        val isZoneBottomSheetVisible: Boolean,
        val loadingMap: Boolean,
    ) : BaseViewDataState {
        data class ZoneData(
            val uuid: String,
            val nameZona: String,
            val minOrderCost: Int?,
            val normalDeliveryCost: Int,
            val forLowDeliveryCost: Int?,
            val deliveryZonePoint: List<Position>,
        )
    }

    enum class State {
        SUCCESS,
        ERROR,
    }

    sealed interface Action : BaseAction {
        data object OnBackClick : Action

        data object OnCloseBottomSheetDeliveryZoneClicked : Action

        data class OnDeliveryZoneClicked(
            val zoneUuid: String,
        ) : Action

        data object LoadAllData : Action

        data class UpdateDeliveryZone(
            val zoneUuid: String,
        ) : Action

        data class OnEditInfoDeliveryZone(
            val zoneUuid: String,
        ) : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event

        data class EditInfoDeliveryZoneEvent(
            val zoneUuid: String,
        ) : Event
    }
}
