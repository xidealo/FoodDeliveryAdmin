package com.bunbeauty.shared.feature.workinghours

import com.bunbeauty.domain.model.cafe.CafeWorkingDay
import com.bunbeauty.shared.viewmodel.base.BaseAction
import com.bunbeauty.shared.viewmodel.base.BaseDataState
import com.bunbeauty.shared.viewmodel.base.BaseEvent

interface WorkingHoursState {
    data class DataState(
        val state: State,
        val workingDays: List<CafeWorkingDay>,
        val isLoading: Boolean,
        val hasInvalidRange: Boolean,
        val timePicker: TimePicker?,
    ) : BaseDataState {
        enum class State {
            LOADING,
            SUCCESS,
            ERROR,
        }

        data class TimePicker(
            val dayOfWeek: Int,
            val field: Field,
            val hour: Int,
            val minute: Int,
        )

        enum class Field {
            FROM,
            TO,
        }
    }

    sealed interface Action : BaseAction {
        data object Init : Action

        data object OnBackClicked : Action

        data object OnSaveClicked : Action

        data class OnFromTimeClicked(
            val dayOfWeek: Int,
        ) : Action

        data class OnToTimeClicked(
            val dayOfWeek: Int,
        ) : Action

        data object OnTimePickerDismissed : Action

        data class OnTimePicked(
            val hour: Int,
            val minute: Int,
        ) : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBackEvent : Event

        data object ShowUpdatedEvent : Event
    }
}
