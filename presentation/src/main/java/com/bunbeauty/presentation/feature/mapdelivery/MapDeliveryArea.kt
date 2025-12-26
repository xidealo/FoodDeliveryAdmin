package com.bunbeauty.presentation.feature.mapdelivery

import com.bunbeauty.domain.model.cafe.DeliveryZonePoint
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState

interface MapDeliveryArea {
    data class DataState(
        val listDeliveryAreaZone: List<ZoneData>,
        val positionCafe: CafeCoordinate?,
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
            val deliveryZonePoint: List<DeliveryZonePoint>,
        )

        data class CafeCoordinate(
            val latitude: Double,
            val longitude: Double
        )
    }

    sealed interface Action : BaseAction {
        data object OnBackClick : Action

        data object OnCloseBottomSheetDeliveryZoneClicked : Action

        data class OnDeliveryZoneClicked(
            val zoneUuid: String,
        ) : Action

        data object LoadAllData : Action

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
