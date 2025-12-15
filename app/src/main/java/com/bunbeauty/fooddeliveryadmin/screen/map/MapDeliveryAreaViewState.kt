package com.bunbeauty.fooddeliveryadmin.screen.map

import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import org.maplibre.spatialk.geojson.Position

data class MapDeliveryAreaViewState(
    // val isLoading: Boolean,
    val listPolygons: List<List<Position>>,
    val positionCafe: Position?,
) : BaseViewState
