package com.bunbeauty.presentation.feature.mapdelivery

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.common.GetCafeUseCase
import com.bunbeauty.domain.feature.mapzonedelivery.GetDeliveryZoneUseCase
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.DeliveryZone
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import org.maplibre.spatialk.geojson.Position

class MapDeliveryAreaViewModel(
    private val getCafeUseCase: GetCafeUseCase,
    private val getDeliveryZoneUseCase: GetDeliveryZoneUseCase,
) : BaseStateViewModel<MapDeliveryArea.DataState, MapDeliveryArea.Action, MapDeliveryArea.Event>(
        initState =
            MapDeliveryArea.DataState(
                // isLoading = true,
                listPolygons = emptyList(),
                positionCafe = null,
                listDeliveryAreaZone = emptyList(),
                loadingMap = false,
                showBottomSheet = false,
            ),
    ) {
    override fun reduce(
        action: MapDeliveryArea.Action,
        dataState: MapDeliveryArea.DataState,
    ) {
        when (action) {
            MapDeliveryArea.Action.OnBackClick ->
                backClick()

            MapDeliveryArea.Action.LoadAllData -> loadAllData()
            MapDeliveryArea.Action.OnCloseBottomSheetDeliveryZoneClicked -> closeZoneBottomSheet()
            is MapDeliveryArea.Action.OnDeliveryZoneClicked -> showZoneBottomSheet(action.zoneIndex)
            is MapDeliveryArea.Action.OnEditInfoDeliveryZone -> editInfoDeliveryZone(zoneUuid = action.zoneUuid)
        }
    }

    private fun backClick() {
        sendEvent { MapDeliveryArea.Event.Back }
    }

    private fun loadAllData() {
        viewModelScope.launchSafe(
            block = {
                val cafe = getCafeUseCase()
                val cafePosition = mapToPositionCafe(cafe)
                val deliveryZones = getDeliveryZoneUseCase()
                val zoneData =
                    deliveryZones.map { zone ->
                        MapDeliveryArea.DataState.ZoneData(
                            minOrderCost = zone.minOrderCost,
                            normalDeliveryCost = zone.normalDeliveryCost,
                            forLowDeliveryCost = zone.forLowDeliveryCost,
                            nameZona = zone.nameZone,
                            uuid = zone.uuid,
                        )
                    }

                val mapPolygons = mapToPositionList(deliveryZones)

                setState {
                    copy(
                        positionCafe = cafePosition,
                        listPolygons = mapPolygons,
                        listDeliveryAreaZone = zoneData,
                        selectedZoneIndex = null,
                        isZoneBottomSheetVisible = false,
                        loadingMap = false,
                    )
                }
            },
            onError = {},
        )
    }

    private fun showZoneBottomSheet(zoneIndex: Int) {
        setState {
            copy(
                showBottomSheet = true,
                selectedZoneIndex = zoneIndex,
                isZoneBottomSheetVisible = true,
            )
        }
    }

    private fun closeZoneBottomSheet() {
        setState {
            copy(
                showBottomSheet = false,
                isZoneBottomSheetVisible = false,
            )
        }
    }

    private fun editInfoDeliveryZone(zoneUuid: String) {
        setState {
            copy(
                showBottomSheet = true,
            )
        }
        sendEvent {
            MapDeliveryArea.Event.EditInfoDeliveryZoneEvent(
                zoneUuid = zoneUuid,
            )
        }
    }

    private fun mapToPositionList(deliveryZones: List<DeliveryZone>): List<List<Position>> =
        deliveryZones.map { deliveryZone ->
            val positions =
                deliveryZone.deliveryZonePoint.map { point ->
                    Position(point.longitude, point.latitude)
                }

            if (positions.isNotEmpty() && positions.first() != positions.last()) {
                positions.plusElement(positions.first())
            } else {
                positions
            }
        }

    private fun mapToPositionCafe(cafeDelivery: Cafe): Position =
        Position(
            longitude = cafeDelivery.longitude,
            latitude = cafeDelivery.latitude,
            altitude = 0.0,
        )
}
