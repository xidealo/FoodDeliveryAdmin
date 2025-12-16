package com.bunbeauty.presentation.feature.mapdelivery

import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseDataState
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import org.maplibre.spatialk.geojson.Position

interface MapDeliveryArea {
    data class DataState(
        //  val isLoading: Boolean,
        // список зон (который в себе содержит данные: цена, мин и тд)
        val listPolygons: List<List<Position>>,
        val positionCafe: Position?,
    ) : BaseDataState{

        // добавить объкт для БС
//        data class ZoneBottomSheet(
//            val isShown: Boolean,
//            val данные, цена, мин и тд
//            val данные, цена, мин и тд
//            val данные, цена, мин и тд
//        )
    }


    sealed interface Action : BaseAction {
        data object OnBackClick : Action

        data object LoadAllData : Action
    }

    sealed interface Event : BaseEvent {
        data object Back : Event
        // Добавить обработчики действий приблизаить "+" и отдалить "-"
    }
}
