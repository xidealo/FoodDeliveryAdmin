package com.bunbeauty.presentation.feature.editcafe

import com.bunbeauty.domain.model.cafe.CafeWorkingHours
import com.bunbeauty.domain.model.nonworkingday.FormattedNonWorkingDay
import com.bunbeauty.presentation.viewmodel.base.ViewDataState
import java.time.LocalDate

data class EditCafeState(
    val cafeUuid: String?,
    val cafeAddress: String,
    val cafeWorkingHours: CafeWorkingHours,
    val nonWorkingDays: NonWorkingDays,
    val initialNonWorkingDayDate: LocalDate,
    val yearRange: IntRange,
    val minNonWorkingDayDate: LocalDate,
): ViewDataState {

    sealed interface NonWorkingDays {
        data object Loading: NonWorkingDays
        data object Empty: NonWorkingDays
        data class Success(
            val days: List<FormattedNonWorkingDay>
        ): NonWorkingDays
    }

}