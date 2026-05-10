package com.bunbeauty.shared.feature.statisticdetails

import com.bunbeauty.domain.model.statistic.StatisticDayDetail
import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseEvent
import com.bunbeauty.shared.viewmodel.base.BaseViewDataState

/**
 * Договор MVI для экрана детализации статистики за день.
 * Разметка и `@Preview` — в файле `StatisticDetailsRoute.kt` ([StatisticDetailsRouteScreen] и превью рядом с ним).
 */
interface StatisticDetails {
    data class DataState(
        val state: State,
        val dayDetail: StatisticDayDetail? = null,
        val loadedDateIso: String = "",
    ) : BaseViewDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR,
        }
    }

    sealed interface Action : BaseAction {
        data class Init(
            val dateIso: String,
        ) : Action

        data object Retry : Action

        data object SelectGoBackClick : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBack : Event
    }
}
