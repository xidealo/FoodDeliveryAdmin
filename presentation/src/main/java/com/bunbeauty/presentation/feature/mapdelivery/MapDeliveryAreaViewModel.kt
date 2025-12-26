package com.bunbeauty.presentation.feature.mapdelivery

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.common.GetCafeUseCase
import com.bunbeauty.domain.feature.mapzonedelivery.GetDeliveryZoneUseCase
import com.bunbeauty.domain.model.cafe.DeliveryZone
import com.bunbeauty.domain.model.cafe.DeliveryZonePoint
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import org.maplibre.spatialk.geojson.Position

class MapDeliveryAreaViewModel(
    private val getCafeUseCase: GetCafeUseCase,
    private val getDeliveryZoneUseCase: GetDeliveryZoneUseCase,
) : BaseStateViewModel<MapDeliveryArea.DataState, MapDeliveryArea.Action, MapDeliveryArea.Event>(
    initState =
        MapDeliveryArea.DataState(
            positionCafe = null,
            listDeliveryAreaZone = emptyList(),
            loadingMap = false,
            isZoneBottomSheetVisible = false
        ),
) {
    override fun reduce(
        action: MapDeliveryArea.Action,
        dataState: MapDeliveryArea.DataState,
    ) {
        when (action) {
            MapDeliveryArea.Action.OnBackClick -> backClick()

            MapDeliveryArea.Action.LoadAllData -> loadAllData()
            MapDeliveryArea.Action.OnCloseBottomSheetDeliveryZoneClicked -> closeZoneBottomSheet()
            is MapDeliveryArea.Action.OnDeliveryZoneClicked -> showZoneBottomSheet(
                zoneUuid = action.zoneUuid,
                zoneList = dataState.listDeliveryAreaZone
            )

            is MapDeliveryArea.Action.OnEditInfoDeliveryZone -> editInfoDeliveryZone(
                zoneUuid = action.zoneUuid
            )
        }
    }

    private fun backClick() {
        sendEvent { MapDeliveryArea.Event.Back }
    }

    private fun loadAllData() {
        viewModelScope.launchSafe(
            block = {
                val cafe = getCafeUseCase()
                val deliveryZones = getDeliveryZoneUseCase()

                val zoneData = deliveryZones.map { zone ->
                    MapDeliveryArea.DataState.ZoneData(
                        minOrderCost = zone.minOrderCost,
                        normalDeliveryCost = zone.normalDeliveryCost,
                        forLowDeliveryCost = zone.forLowDeliveryCost,
                        nameZona = zone.nameZone,
                        uuid = zone.uuid,
                        deliveryZonePoint = getFullDeliveryZonePointListUseCase(deliveryZone = zone)
                            .map {
                                Position(
                                    latitude = it.latitude,
                                    longitude = it.longitude
                                )
                            }
                    )
                }

                setState {
                    copy(
                        positionCafe = Position(
                            longitude = cafe.longitude,
                            latitude = cafe.latitude,
                        ),
                        listDeliveryAreaZone = zoneData,
                        loadingMap = false,
                    )
                }
            },
            onError = {
                // добавить обработку ошибки
            },
        )
    }

    private fun showZoneBottomSheet(
        zoneUuid: String,
        zoneList: List<MapDeliveryArea.DataState.ZoneData>
    ) {
        setState {
            copy(
                selectedZoneData = zoneList.find { zoneData ->
                    zoneData.uuid == zoneUuid
                },
                isZoneBottomSheetVisible = true,
            )
        }
    }

    private fun closeZoneBottomSheet() {
        setState {
            copy(
                isZoneBottomSheetVisible = false,
            )
        }
    }

    private fun editInfoDeliveryZone(zoneUuid: String) {
        setState {
            copy(
                isZoneBottomSheetVisible = true,
            )
        }
        sendEvent {
            MapDeliveryArea.Event.EditInfoDeliveryZoneEvent(
                zoneUuid = zoneUuid,
            )
        }
    }

    // вынести в юзкейс и написать тест
    private fun getFullDeliveryZonePointListUseCase(deliveryZone: DeliveryZone): List<DeliveryZonePoint> {
        val positions = deliveryZone.deliveryZonePoint
        return if (positions.isNotEmpty() && positions.first() != positions.last()) {
            positions.plusElement(positions.first())
        } else {
            positions
        }
    }
}
