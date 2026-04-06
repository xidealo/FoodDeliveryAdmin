package com.bunbeauty.presentation.feature.mapdelivery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.presentation.feature.mapdelivery.MapDeliveryZoneViewState.State.Error
import com.bunbeauty.presentation.feature.mapdelivery.MapDeliveryZoneViewState.State.Success
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.maplibre.spatialk.geojson.Position

@Immutable
data class MapDeliveryZoneViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val listDeliveryAreaZone: ImmutableList<ZoneData>,
            val positionCafe: Position?,
            val selectedZoneData: ZoneData?,
            val loadingMap: Boolean,
        ) : State
    }

    @Immutable
    data class ZoneData(
        val uuid: String,
        val nameZona: String,
        val minOrderCost: Int?,
        val normalDeliveryCost: Int,
        val forLowDeliveryCost: Int?,
        val deliveryZonePoint: List<Position>,
    )
}

@Composable
internal fun MapDeliveryZone.DataState.toViewState(): MapDeliveryZoneViewState =
    MapDeliveryZoneViewState(
        state =
            when (state) {
                MapDeliveryZone.State.ERROR -> Error
                MapDeliveryZone.State.SUCCESS -> {
                    Success(
                        listDeliveryAreaZone =
                            listDeliveryAreaZone
                                .map {
                                    MapDeliveryZoneViewState.ZoneData(
                                        uuid = it.uuid,
                                        nameZona = it.nameZona,
                                        minOrderCost = it.minOrderCost,
                                        normalDeliveryCost = it.normalDeliveryCost,
                                        forLowDeliveryCost = it.forLowDeliveryCost,
                                        deliveryZonePoint = it.deliveryZonePoint,
                                    )
                                }.toPersistentList(),
                        positionCafe = positionCafe,
                        selectedZoneData =
                            selectedZoneData?.let {
                                MapDeliveryZoneViewState.ZoneData(
                                    uuid = it.uuid,
                                    nameZona = it.nameZona,
                                    minOrderCost = it.minOrderCost,
                                    normalDeliveryCost = it.normalDeliveryCost,
                                    forLowDeliveryCost = it.forLowDeliveryCost,
                                    deliveryZonePoint = it.deliveryZonePoint,
                                )
                            },
                        loadingMap = loadingMap,
                    )
                }
            },
    )

private object MapColors {
    val polygonColors =
        listOf(
            Color(0xFFFF5252), // Red
            Color(0xFF9C27B0), // Purple
            Color(0xFFFFAB40), // Light Orange
            Color(0xFF42A5F5), // Blue
            Color(0xFF66BB6A), // Green
        )

    fun generatePolygonColor(index: Int): Color = polygonColors[index % polygonColors.size]
}

@Composable
internal fun generatePolygonColors(count: Int): List<Color> = (0 until count).map { MapColors.generatePolygonColor(it) }
