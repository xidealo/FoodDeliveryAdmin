package com.bunbeauty.presentation.feature.editcafe

import androidx.lifecycle.viewModelScope
import com.bunbeauty.domain.feature.editcafe.CreateCafeNonWorkingDayUseCase
import com.bunbeauty.domain.feature.editcafe.DeleteCafeNonWorkingDayUseCase
import com.bunbeauty.domain.feature.editcafe.GetCafeWorkingHoursByUuidUseCase
import com.bunbeauty.domain.feature.editcafe.GetInitialNonWorkingDayDateUseCase
import com.bunbeauty.domain.feature.editcafe.GetMinNonWorkingDayDateUseCase
import com.bunbeauty.domain.feature.editcafe.GetNonWorkingDayListByCafeUuidUseCase
import com.bunbeauty.domain.feature.editcafe.GetNonWorkingDayYearRangeUseCase
import com.bunbeauty.domain.feature.editcafe.UpdateCafeFromTimeUseCase
import com.bunbeauty.domain.feature.editcafe.UpdateCafeToTimeUseCase
import com.bunbeauty.domain.model.cafe.CafeWorkingHours
import com.bunbeauty.domain.util.datetime.DateTimeUtil
import com.bunbeauty.presentation.extension.launchSafe
import com.bunbeauty.presentation.viewmodel.base.BaseStateViewModel
import java.time.LocalDate
import java.time.LocalTime

class EditCafeViewModel(
    private val getCafeWorkingHoursByUuid: GetCafeWorkingHoursByUuidUseCase,
    private val getNonWorkingDayListByCafeUuid: GetNonWorkingDayListByCafeUuidUseCase,
    private val updateCafeFromTime: UpdateCafeFromTimeUseCase,
    private val updateCafeToTime: UpdateCafeToTimeUseCase,
    private val createCafeNonWorkingDay: CreateCafeNonWorkingDayUseCase,
    private val deleteCafeNonWorkingDay: DeleteCafeNonWorkingDayUseCase,
    private val dateTimeUtil: DateTimeUtil,
    getInitialNonWorkingDayDate: GetInitialNonWorkingDayDateUseCase,
    getNonWorkingDayYearRange: GetNonWorkingDayYearRangeUseCase,
    getMinNonWorkingDayDate: GetMinNonWorkingDayDateUseCase,
) : BaseStateViewModel<EditCafeState.ViewDataState, EditCafeState.Action, EditCafeState.Event>(
        initState =
            EditCafeState.ViewDataState(
                cafeUuid = null,
                cafeAddress = "",
                cafeWorkingHours =
                    CafeWorkingHours(
                        fromTimeText = "",
                        fromTime = LocalTime.MIN,
                        toTimeText = "",
                        toTime = LocalTime.MAX,
                    ),
                nonWorkingDays = EditCafeState.ViewDataState.NonWorkingDays.Loading,
                initialNonWorkingDayDate = getInitialNonWorkingDayDate(),
                yearRange = getNonWorkingDayYearRange(),
                minNonWorkingDayDate = getMinNonWorkingDayDate(),
            ),
    ) {
    override fun reduce(
        action: EditCafeState.Action,
        dataState: EditCafeState.ViewDataState,
    ) {
        when (action) {
            is EditCafeState.Action.Init -> {
                setState {
                    copy(
                        cafeUuid = action.cafeUuid,
                        cafeAddress = action.cafeAddress,
                    )
                }
                fetchCafeData(action.cafeUuid)
            }

            is EditCafeState.Action.UpdateFromTime -> {
                updateFromTime(action.time)
            }

            is EditCafeState.Action.UpdateToTime -> {
                updateToTime(action.time)
            }

            is EditCafeState.Action.AddNonWorkingDay -> {
                addNonWorkingDay(action.date)
            }

            is EditCafeState.Action.DeleteNonWorkingDay -> {
                requestConfirmDelete(action.uuid)
            }

            is EditCafeState.Action.ConfirmDeleteNonWorkingDay -> {
                deleteNonWorkingDay(action.uuid)
            }

            EditCafeState.Action.BackClick -> {
                sendEvent {
                    EditCafeState.Event.GoBack
                }
            }
        }
    }

    private fun fetchCafeData(cafeUuid: String) {
        viewModelScope.launchSafe(
            block = {
                val cafeWorkingHours = getCafeWorkingHoursByUuid(cafeUuid)
                val nonWorkingDayList = getNonWorkingDayListByCafeUuid(cafeUuid)
                setState {
                    copy(
                        cafeWorkingHours = cafeWorkingHours,
                        nonWorkingDays =
                            if (nonWorkingDayList.isEmpty()) {
                                EditCafeState.ViewDataState.NonWorkingDays.Empty
                            } else {
                                EditCafeState.ViewDataState.NonWorkingDays.Success(
                                    days = nonWorkingDayList,
                                )
                            },
                    )
                }
            },
            onError = {
                setState {
                    copy(
                        nonWorkingDays = EditCafeState.ViewDataState.NonWorkingDays.Empty,
                    )
                }
                sendEvent {
                    EditCafeState.Event.ShowFetchDataError
                }
            },
        )
    }

    private fun updateFromTime(time: LocalTime) {
        val cafeUuid = state.value.cafeUuid ?: return
        viewModelScope.launchSafe(
            block = {
                updateCafeFromTime(cafeUuid, time)
                setState {
                    copy(
                        cafeWorkingHours =
                            state.value.cafeWorkingHours.copy(
                                fromTimeText = dateTimeUtil.getTimeHHMM(time),
                                fromTime = time,
                            ),
                    )
                }
            },
            onError = {
                sendEvent {
                    EditCafeState.Event.ShowUpdateDataError
                }
            },
        )
    }

    private fun updateToTime(time: LocalTime) {
        val cafeUuid = state.value.cafeUuid ?: return
        viewModelScope.launchSafe(
            block = {
                updateCafeToTime(cafeUuid, time)
                setState {
                    copy(
                        cafeWorkingHours =
                            cafeWorkingHours.copy(
                                toTimeText = dateTimeUtil.getTimeHHMM(time),
                                toTime = time,
                            ),
                    )
                }
            },
            onError = {
                sendEvent {
                    EditCafeState.Event.ShowUpdateDataError
                }
            },
        )
    }

    private fun addNonWorkingDay(date: LocalDate) {
        val cafeUuid = state.value.cafeUuid ?: return

        viewModelScope.launchSafe(
            block = {
                createCafeNonWorkingDay(
                    date = date,
                    cafeUuid = cafeUuid,
                )
                val nonWorkingDayList = getNonWorkingDayListByCafeUuid(cafeUuid)
                setState {
                    copy(
                        nonWorkingDays =
                            EditCafeState.ViewDataState.NonWorkingDays.Success(
                                days = nonWorkingDayList,
                            ),
                    )
                }
            },
            onError = {
                sendEvent {
                    EditCafeState.Event.ShowSaveDataError
                }
            },
        )
    }

    private fun requestConfirmDelete(uuid: String) {
        sendEvent {
            EditCafeState.Event.ShowConfirmDeletion(uuid = uuid)
        }
    }

    private fun deleteNonWorkingDay(uuid: String) {
        val cafeUuid = state.value.cafeUuid ?: return
        viewModelScope.launchSafe(
            block = {
                deleteCafeNonWorkingDay(uuid)
                val nonWorkingDayList = getNonWorkingDayListByCafeUuid(cafeUuid)
                setState {
                    copy(
                        nonWorkingDays =
                            if (nonWorkingDayList.isEmpty()) {
                                EditCafeState.ViewDataState.NonWorkingDays.Empty
                            } else {
                                EditCafeState.ViewDataState.NonWorkingDays.Success(
                                    days = nonWorkingDayList,
                                )
                            },
                    )
                }
            },
            onError = {
                sendEvent {
                    EditCafeState.Event.ShowDeleteDataError
                }
            },
        )
    }
}
