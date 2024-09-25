package com.bunbeauty.presentation.feature.editcafe

import com.bunbeauty.domain.model.cafe.CafeWorkingHours
import com.bunbeauty.domain.model.nonworkingday.FormattedNonWorkingDay
import com.bunbeauty.presentation.viewmodel.base.BaseAction
import com.bunbeauty.presentation.viewmodel.base.BaseEvent
import com.bunbeauty.presentation.viewmodel.base.BaseViewDataState
import java.time.LocalDate
import java.time.LocalTime

interface EditCafe {

    data class ViewDataState(
        val cafeUuid: String?,
        val cafeAddress: String,
        val cafeWorkingHours: CafeWorkingHours,
        val nonWorkingDays: NonWorkingDays,
        val initialNonWorkingDayDate: LocalDate,
        val yearRange: IntRange,
        val minNonWorkingDayDate: LocalDate
    ) : BaseViewDataState {

        sealed interface NonWorkingDays {
            data object Loading : NonWorkingDays
            data object Empty : NonWorkingDays
            data class Success(
                val days: List<FormattedNonWorkingDay>
            ) : NonWorkingDays
        }
    }

    sealed interface Action : BaseAction {

        data class Init(
            val cafeUuid: String,
            val cafeAddress: String
        ) : Action

        data class UpdateFromTime(val time: LocalTime) : Action
        data class UpdateToTime(val time: LocalTime) : Action
        data class AddNonWorkingDay(val date: LocalDate) : Action
        data class DeleteNonWorkingDay(val uuid: String) : Action
        data class ConfirmDeleteNonWorkingDay(val uuid: String) : Action
        data object BackClick : Action
    }

    sealed interface Event : BaseEvent {
        data object GoBack : Event
        data object ShowFetchDataError : Event
        data object ShowUpdateDataError : Event
        data object ShowSaveDataError : Event
        data object ShowDeleteDataError : Event
        data class ShowConfirmDeletion(val uuid: String) : Event
    }
}
