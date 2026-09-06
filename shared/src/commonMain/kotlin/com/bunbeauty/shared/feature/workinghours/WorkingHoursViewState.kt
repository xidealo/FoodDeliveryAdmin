package com.bunbeauty.shared.feature.workinghours

import DateTimeUtil
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.bunbeauty.shared.viewmodel.base.BaseViewState
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_friday
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_monday
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_saturday
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_sunday
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_thursday
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_tuesday
import fooddeliveryadmin.shared.generated.resources.msg_working_hours_wednesday
import org.jetbrains.compose.resources.StringResource

@Immutable
data class WorkingHoursViewState(
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val days: List<Day>,
            val isLoading: Boolean,
            val timePicker: TimePicker?,
        ) : State
    }

    @Immutable
    data class Day(
        val dayOfWeek: Int,
        val titleResId: StringResource,
        val fromTime: String,
        val toTime: String,
        val isInvalid: Boolean,
    )

    @Immutable
    data class TimePicker(
        val hour: Int,
        val minute: Int,
    )
}

@Composable
internal fun WorkingHoursState.DataState.toViewState(): WorkingHoursViewState =
    WorkingHoursViewState(
        state =
            when (state) {
                WorkingHoursState.DataState.State.LOADING -> WorkingHoursViewState.State.Loading
                WorkingHoursState.DataState.State.ERROR -> WorkingHoursViewState.State.Error
                WorkingHoursState.DataState.State.SUCCESS ->
                    WorkingHoursViewState.State.Success(
                        days =
                            workingDays.map { workingDay ->
                                WorkingHoursViewState.Day(
                                    dayOfWeek = workingDay.dayOfWeek,
                                    titleResId = workingDay.dayOfWeek.toDayTitleResId(),
                                    fromTime = DateTimeUtil.getTimeHHMM(workingDay.fromTime),
                                    toTime = DateTimeUtil.getTimeHHMM(workingDay.toTime),
                                    isInvalid = hasInvalidRange && workingDay.fromTime >= workingDay.toTime,
                                )
                            },
                        isLoading = isLoading,
                        timePicker =
                            timePicker?.let { picker ->
                                WorkingHoursViewState.TimePicker(
                                    hour = picker.hour,
                                    minute = picker.minute,
                                )
                            },
                    )
            },
    )

private fun Int.toDayTitleResId(): StringResource =
    when (this) {
        1 -> Res.string.msg_working_hours_monday
        2 -> Res.string.msg_working_hours_tuesday
        3 -> Res.string.msg_working_hours_wednesday
        4 -> Res.string.msg_working_hours_thursday
        5 -> Res.string.msg_working_hours_friday
        6 -> Res.string.msg_working_hours_saturday
        7 -> Res.string.msg_working_hours_sunday
        else -> Res.string.msg_working_hours_monday
    }
