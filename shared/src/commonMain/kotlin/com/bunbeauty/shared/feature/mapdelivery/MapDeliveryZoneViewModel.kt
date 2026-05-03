package com.bunbeauty.shared.feature.mapdelivery

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.common.GetCafeUseCase
import com.bunbeauty.domain.feature.mapzonedelivery.GetDeliveryZoneUseCase
import com.bunbeauty.domain.feature.mapzonedelivery.editinfodeliveryzone.GetFullDeliveryZonePointListUseCase
import com.bunbeauty.domain.feature.mapzonedelivery.editinfodeliveryzone.GetZoneUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel
import org.maplibre.spatialk.geojson.Position
import kotlin.String

class MapDeliveryZoneViewModel(
    private val getCafeUseCase: GetCafeUseCase,
    private val getZoneUseCase: GetZoneUseCase,
    private val getDeliveryZoneUseCase: GetDeliveryZoneUseCase,
    private val getFullDeliveryZonePointListUseCase: GetFullDeliveryZonePointListUseCase,
) : BaseStateViewModel<MapDeliveryZone.DataState, MapDeliveryZone.Action, MapDeliveryZone.Event>(
        initState =
            MapDeliveryZone.DataState(
                positionCafe = null,
                listDeliveryAreaZone = emptyList(),
                loadingMap = false,
                isZoneBottomSheetVisible = false,
                state = MapDeliveryZone.State.SUCCESS,
            ),
    ) {
    override fun reduce(
        action: MapDeliveryZone.Action,
        dataState: MapDeliveryZone.DataState,
    ) {
        when (action) {
            MapDeliveryZone.Action.OnBackClick -> backClick()

            MapDeliveryZone.Action.LoadAllData -> loadAllData()
            MapDeliveryZone.Action.OnCloseBottomSheetDeliveryZoneClicked -> closeZoneBottomSheet()
            is MapDeliveryZone.Action.OnDeliveryZoneClicked ->
                showZoneBottomSheet(
                    zoneUuid = action.zoneUuid,
                    zoneList = dataState.listDeliveryAreaZone,
                )

            is MapDeliveryZone.Action.OnEditInfoDeliveryZone ->
                editInfoDeliveryZone(
                    zoneUuid = action.zoneUuid,
                )

            is MapDeliveryZone.Action.UpdateDeliveryZone ->
                updateSelectedDeliveryZone(
                    zoneUuid = action.zoneUuid,
                )
        }
    }

    private fun backClick() {
        sendEvent { MapDeliveryZone.Event.Back }
    }

    private fun loadAllData() {
        viewModelScope.launchSafe(
            block = {
                val cafe = getCafeUseCase()
                val deliveryZones = getDeliveryZoneUseCase()
                val zoneData =
                    deliveryZones.map { zone ->
                        MapDeliveryZone.DataState.ZoneData(
                            minOrderCost = zone.minOrderCost,
                            normalDeliveryCost = zone.normalDeliveryCost,
                            forLowDeliveryCost = zone.forLowDeliveryCost,
                            nameZona = zone.nameZone,
                            uuid = zone.uuid,
                            deliveryZonePoint =
                                getFullDeliveryZonePointListUseCase(deliveryZone = zone)
                                    .map {
                                        Position(
                                            latitude = it.latitude,
                                            longitude = it.longitude,
                                        )
                                    },
                        )
                    }

                setState {
                    copy(
                        positionCafe =
                            Position(
                                longitude = cafe.longitude,
                                latitude = cafe.latitude,
                            ),
                        listDeliveryAreaZone = zoneData,
                        loadingMap = false,
                    )
                }
            },
            onError = {
                setState {
                    copy(state = MapDeliveryZone.State.ERROR)
                }
            },
        )
    }

    private fun showZoneBottomSheet(
        zoneUuid: String,
        zoneList: List<MapDeliveryZone.DataState.ZoneData>,
    ) {
        setState {
            copy(
                selectedZoneData =
                    zoneList.find { zoneData ->
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
            MapDeliveryZone.Event.EditInfoDeliveryZoneEvent(
                zoneUuid = zoneUuid,
            )
        }
    }

    private fun updateSelectedDeliveryZone(zoneUuid: String) {
        viewModelScope.launchSafe(
            block = {
                val zone = getZoneUseCase(zoneUuid = zoneUuid)
                val listDeliveryAreaZone =
                    MapDeliveryZone.DataState.ZoneData(
                        nameZona = zone.nameZone,
                        uuid = zone.uuid,
                        minOrderCost = zone.minOrderCost,
                        normalDeliveryCost = zone.normalDeliveryCost,
                        forLowDeliveryCost = zone.forLowDeliveryCost,
                        deliveryZonePoint =
                            getFullDeliveryZonePointListUseCase(deliveryZone = zone)
                                .map {
                                    Position(
                                        latitude = it.latitude,
                                        longitude = it.longitude,
                                    )
                                },
                    )

                setState {
                    copy(
                        selectedZoneData = listDeliveryAreaZone,
                    )
                }
            },
            onError = {
                setState {
                    copy(state = MapDeliveryZone.State.ERROR)
                }
            },
        )
    }
}
