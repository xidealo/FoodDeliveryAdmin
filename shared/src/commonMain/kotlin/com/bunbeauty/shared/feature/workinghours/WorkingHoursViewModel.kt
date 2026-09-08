package com.bunbeauty.shared.feature.workinghours

import DateTimeUtil
import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.cafe.GetCafeWorkingDaysUseCase
import com.bunbeauty.domain.feature.cafe.InvalidCafeWorkingHoursException
import com.bunbeauty.domain.feature.cafe.UpdateCafeWorkingDaysUseCase
import com.bunbeauty.shared.extension.launchSafe
import com.bunbeauty.shared.viewmodel.base.BaseStateViewModel

class WorkingHoursViewModel(
    private val getCafeWorkingDaysUseCase: GetCafeWorkingDaysUseCase,
    private val updateCafeWorkingDaysUseCase: UpdateCafeWorkingDaysUseCase,
) : BaseStateViewModel<WorkingHoursState.DataState, WorkingHoursState.Action, WorkingHoursState.Event>(
        initState =
            WorkingHoursState.DataState(
                state = WorkingHoursState.DataState.State.LOADING,
                workingDays = emptyList(),
                isLoading = false,
                hasInvalidRange = false,
                timePicker = null,
            ),
    ) {
    override fun reduce(
        action: WorkingHoursState.Action,
        dataState: WorkingHoursState.DataState,
    ) {
        when (action) {
            WorkingHoursState.Action.Init -> loadData()
            WorkingHoursState.Action.OnBackClicked -> onBackClicked()
            WorkingHoursState.Action.OnSaveClicked -> saveWorkingHours(dataState)
            is WorkingHoursState.Action.OnFromTimeClicked ->
                showTimePicker(
                    dayOfWeek = action.dayOfWeek,
                    field = WorkingHoursState.DataState.Field.FROM,
                    dataState = dataState,
                )

            is WorkingHoursState.Action.OnToTimeClicked ->
                showTimePicker(
                    dayOfWeek = action.dayOfWeek,
                    field = WorkingHoursState.DataState.Field.TO,
                    dataState = dataState,
                )

            WorkingHoursState.Action.OnTimePickerDismissed -> hideTimePicker()
            is WorkingHoursState.Action.OnTimePicked ->
                applyPickedTime(
                    hour = action.hour,
                    minute = action.minute,
                    dataState = dataState,
                )
        }
    }

    private fun loadData() {
        setState {
            copy(state = WorkingHoursState.DataState.State.LOADING)
        }
        viewModelScope.launchSafe(
            block = {
                val workingDays = getCafeWorkingDaysUseCase()
                setState {
                    copy(
                        state = WorkingHoursState.DataState.State.SUCCESS,
                        workingDays = workingDays,
                        isLoading = false,
                        hasInvalidRange = false,
                    )
                }
            },
            onError = {
                setState {
                    copy(state = WorkingHoursState.DataState.State.ERROR)
                }
            },
        )
    }

    private fun onBackClicked() {
        sendEvent {
            WorkingHoursState.Event.GoBackEvent
        }
    }

    private fun showTimePicker(
        dayOfWeek: Int,
        field: WorkingHoursState.DataState.Field,
        dataState: WorkingHoursState.DataState,
    ) {
        if (dataState.isLoading) return
        val workingDay =
            dataState.workingDays.firstOrNull { day ->
                day.dayOfWeek == dayOfWeek
            } ?: return
        val daySeconds =
            if (field == WorkingHoursState.DataState.Field.FROM) {
                workingDay.fromTime
            } else {
                workingDay.toTime
            }
        setState {
            copy(
                timePicker =
                    WorkingHoursState.DataState.TimePicker(
                        dayOfWeek = dayOfWeek,
                        field = field,
                        hour = DateTimeUtil.getHour(daySeconds),
                        minute = DateTimeUtil.getMinute(daySeconds),
                    ),
            )
        }
    }

    private fun hideTimePicker() {
        setState {
            copy(timePicker = null)
        }
    }

    private fun applyPickedTime(
        hour: Int,
        minute: Int,
        dataState: WorkingHoursState.DataState,
    ) {
        if (dataState.isLoading) return
        val timePicker = dataState.timePicker ?: return
        val daySeconds = DateTimeUtil.getDaySeconds(hour = hour, minute = minute)
        setState {
            copy(
                workingDays =
                    workingDays.map { workingDay ->
                        if (workingDay.dayOfWeek == timePicker.dayOfWeek) {
                            if (timePicker.field == WorkingHoursState.DataState.Field.FROM) {
                                workingDay.copy(fromTime = daySeconds)
                            } else {
                                workingDay.copy(toTime = daySeconds)
                            }
                        } else {
                            workingDay
                        }
                    },
                timePicker = null,
                hasInvalidRange = false,
            )
        }
    }

    private fun saveWorkingHours(dataState: WorkingHoursState.DataState) {
        viewModelScope.launchSafe(
            block = {
                setState {
                    copy(isLoading = true, hasInvalidRange = false)
                }
                updateCafeWorkingDaysUseCase(dataState.workingDays)
                sendEvent {
                    WorkingHoursState.Event.ShowUpdatedEvent
                }
            },
            onError = { throwable ->
                setState {
                    if (throwable is InvalidCafeWorkingHoursException) {
                        copy(
                            isLoading = false,
                            hasInvalidRange = true,
                        )
                    } else {
                        copy(
                            isLoading = false,
                            state = WorkingHoursState.DataState.State.ERROR,
                        )
                    }
                }
            },
        )
    }
}
