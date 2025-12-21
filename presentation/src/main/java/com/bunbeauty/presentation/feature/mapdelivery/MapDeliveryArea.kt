package com.bunbeauty.presentation.feature.mapdelivery

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState
import org.maplibre.spatialk.geojson.Position

interface MapDeliveryArea {
    data class DataState(
        val listDeliveryAreaZone: List<ZoneData>,
        val listPolygons: List<List<Position>>,
        val positionCafe: Position?,
        val selectedZoneIndex: Int? = null,
        val isZoneBottomSheetVisible: Boolean = false,
        val loadingMap: Boolean,
    ) : BaseViewDataState {
        data class ZoneData(
            val nameZona: String,
            val minOrderCost: Int?,
            val normalDeliveryCost: Int,
            val forLowDeliveryCost: Int?,
        )
    }

    sealed interface Action : BaseAction {
        data object OnBackClick : Action

        data object OnCloseBottomSheetDeliveryZoneClicked : Action

        data class OnDeliveryZoneClicked(
            val zoneIndex: Int,
        ) : Action

        data object LoadAllData : Action

        data object OnEditInfoDeliveryZone : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event

        data object EditInfoDeliveryZoneEvent : Event
    }
}
