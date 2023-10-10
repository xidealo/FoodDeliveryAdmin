package com.bunbeauty.presentation.feature.editcafe

import com.bunbeauty.presentation.viewmodel.base.Action
import java.time.LocalDate
import java.time.LocalTime

sealed interface EditCafeAction : Action {

    data class Init(
        val cafeUuid: String,
        val cafeAddress: String,
    ) : EditCafeAction

    data class UpdateFromTime(val time: LocalTime) : EditCafeAction
    data class UpdateToTime(val time: LocalTime) : EditCafeAction
    data class AddNonWorkingDay(val date: LocalDate) : EditCafeAction
    data class DeleteNonWorkingDay(val uuid: String) : EditCafeAction
    data class ConfirmDeleteNonWorkingDay(val uuid: String) : EditCafeAction
    data object BackClick : EditCafeAction
}