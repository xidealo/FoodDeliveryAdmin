package com.bunbeauty.presentation.feature.mapdelivery

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.common.GetCafeUseCase
import com.bunbeauty.domain.feature.mapzonedelivery.GetPolygonsDeliveryZoneUseCase
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.CafeDeliveryZone
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import org.maplibre.spatialk.geojson.Position

class MapDeliveryAreaViewModel(
    private val getCafeUseCase: GetCafeUseCase,
    private val getPolygonsDeliveryZoneUseCase: GetPolygonsDeliveryZoneUseCase,
) : BaseStateViewModel<MapDeliveryArea.DataState, MapDeliveryArea.Action, MapDeliveryArea.Event>(
        initState =
            MapDeliveryArea.DataState(
                // isLoading = true,
                listPolygons = emptyList(),
                positionCafe = null,
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
                val cafeDeliveryZones = getPolygonsDeliveryZoneUseCase()
                val mapPolygons = mapToPositionList(cafeDeliveryZones)
                setState {
                    copy(
                        positionCafe = cafePosition,
                        listPolygons = mapPolygons,
                    )
                }
            },
            onError = {},
        )
    }

    private fun mapToPositionList(cafeDeliveryZones: List<List<CafeDeliveryZone>>): List<List<Position>> =
        cafeDeliveryZones.map { polygon ->
            val positions =
                polygon.map { cafeZone ->
                    Position(cafeZone.longitude, cafeZone.latitude)
                }

            if (positions.isNotEmpty() && positions.first() != positions.last()) {
                positions.plusElement(positions.first())
            } else {
                positions
            }
        }

    private fun mapToPositionCafe(cafeDelivery: Cafe): Position =
        Position(longitude = cafeDelivery.longitude, latitude = cafeDelivery.latitude, altitude = 0.0)
}
