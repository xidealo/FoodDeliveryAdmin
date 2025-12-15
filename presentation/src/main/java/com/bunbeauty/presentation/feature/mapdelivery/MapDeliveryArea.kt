package com.bunbeauty.presentation.feature.mapdelivery

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import org.maplibre.spatialk.geojson.Position

interface MapDeliveryArea {
    data class DataState(
        //  val isLoading: Boolean,
        val listPolygons: List<List<Position>>,
        val positionCafe: Position?,
    ) : BaseDataState

    sealed interface Action : BaseAction {
        data object OnBackClick : Action

        data object LoadAllData : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        // Добавить обработчики действий приблизаить "+" и отдалить "-"
    }
}
