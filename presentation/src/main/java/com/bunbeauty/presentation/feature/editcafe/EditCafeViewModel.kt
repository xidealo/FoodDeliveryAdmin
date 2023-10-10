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
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class EditCafeViewModel @Inject constructor(
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
) : BaseStateViewModel<EditCafeState, EditCafeAction, EditCafeEvent>(
    initState = EditCafeState(
        cafeUuid = null,
        cafeAddress = "",
        cafeWorkingHours = CafeWorkingHours(
            fromTimeText = "",
            fromTime = LocalTime.MIN,
            toTimeText = "",
            toTime = LocalTime.MAX,
        ),
        nonWorkingDays = EditCafeState.NonWorkingDays.Loading,
        initialNonWorkingDayDate = getInitialNonWorkingDayDate(),
        yearRange = getNonWorkingDayYearRange(),
        minNonWorkingDayDate = getMinNonWorkingDayDate(),
    )
) {

    override fun handleAction(action: EditCafeAction) {
        when (action) {
            is EditCafeAction.Init -> {
                state { state ->
                    state.copy(
                        cafeUuid = action.cafeUuid,
                        cafeAddress = action.cafeAddress,
                    )
                }
                fetchCafeData(action.cafeUuid)
            }

            is EditCafeAction.UpdateFromTime -> {
                updateFromTime(action.time)
            }

            is EditCafeAction.UpdateToTime -> {
                updateToTime(action.time)
            }

            is EditCafeAction.AddNonWorkingDay -> {
                addNonWorkingDay(action.date)
            }

            is EditCafeAction.DeleteNonWorkingDay -> {
                requestConfirmDelete(action.uuid)
            }

            is EditCafeAction.ConfirmDeleteNonWorkingDay -> {
                deleteNonWorkingDay(action.uuid)
            }

            EditCafeAction.BackClick -> {
                event {
                    EditCafeEvent.GoBack
                }
            }
        }
    }

    private fun fetchCafeData(cafeUuid: String) {
        viewModelScope.launchSafe(
            block = {
                val cafeWorkingHours = getCafeWorkingHoursByUuid(cafeUuid)
                val nonWorkingDayList = getNonWorkingDayListByCafeUuid(cafeUuid)
                state { state ->
                    state.copy(
                        cafeWorkingHours = cafeWorkingHours,
                        nonWorkingDays = if (nonWorkingDayList.isEmpty()) {
                            EditCafeState.NonWorkingDays.Empty
                        } else {
                            EditCafeState.NonWorkingDays.Success(
                                days = nonWorkingDayList
                            )
                        },
                    )
                }
            },
            onError = {
                state { state ->
                    state.copy(
                        nonWorkingDays = EditCafeState.NonWorkingDays.Empty,
                    )
                }
                event {
                    EditCafeEvent.ShowFetchDataError
                }
            }
        )
    }

    private fun updateFromTime(time: LocalTime) {
        val cafeUuid = state.value.cafeUuid ?: return
        viewModelScope.launchSafe(
            block = {
                updateCafeFromTime(cafeUuid, time)
                state { state ->
                    state.copy(
                        cafeWorkingHours = state.cafeWorkingHours.copy(
                            fromTimeText = dateTimeUtil.getTimeHHMM(time),
                            fromTime = time,
                        )
                    )
                }
            },
            onError = {
                event {
                    EditCafeEvent.ShowUpdateDataError
                }
            }
        )
    }

    private fun updateToTime(time: LocalTime) {
        val cafeUuid = state.value.cafeUuid ?: return
        viewModelScope.launchSafe(
            block = {
                updateCafeToTime(cafeUuid, time)
                state { state ->
                    state.copy(
                        cafeWorkingHours = state.cafeWorkingHours.copy(
                            toTimeText = dateTimeUtil.getTimeHHMM(time),
                            toTime = time,
                        )
                    )
                }
            },
            onError = {
                event {
                    EditCafeEvent.ShowUpdateDataError
                }
            }
        )
    }

    private fun addNonWorkingDay(date: LocalDate) {
        val cafeUuid = state.value.cafeUuid ?: return

        viewModelScope.launchSafe(
            block = {
                createCafeNonWorkingDay(
                    date = date,
                    cafeUuid = cafeUuid
                )
                val nonWorkingDayList = getNonWorkingDayListByCafeUuid(cafeUuid)
                state { state ->
                    state.copy(
                        nonWorkingDays = EditCafeState.NonWorkingDays.Success(
                            days = nonWorkingDayList
                        ),
                    )
                }
            },
            onError = {
                event {
                    EditCafeEvent.ShowSaveDataError
                }
            }
        )
    }

    private fun requestConfirmDelete(uuid: String) {
        event {
            EditCafeEvent.ShowConfirmDeletion(uuid = uuid)
        }
    }

    private fun deleteNonWorkingDay(uuid: String) {
        val cafeUuid = state.value.cafeUuid ?: return
        viewModelScope.launchSafe(
            block = {
                deleteCafeNonWorkingDay(uuid)
                val nonWorkingDayList = getNonWorkingDayListByCafeUuid(cafeUuid)
                state { state ->
                    state.copy(
                        nonWorkingDays = if (nonWorkingDayList.isEmpty()) {
                            EditCafeState.NonWorkingDays.Empty
                        } else {
                            EditCafeState.NonWorkingDays.Success(
                                days = nonWorkingDayList
                            )
                        },
                    )
                }
            },
            onError = {
                event {
                    EditCafeEvent.ShowDeleteDataError
                }
            }
        )
    }

}