package com.bunbeauty.domain.feature.cafe

import com.bunbeauty.domain.exception.NoCafeException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.cafe.CafeWorkingDay
import com.bunbeauty.domain.model.cafe.UpdateCafe
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

class UpdateCafeWorkingDaysUseCase(
    private val cafeRepo: CafeRepo,
    private val dataStoreRepo: DataStoreRepo,
) {
    suspend operator fun invoke(workingDays: List<CafeWorkingDay>) {
        val dayOfWeekSet = workingDays.map { workingDay -> workingDay.dayOfWeek }.toSet()
        if (dayOfWeekSet != FULL_WEEK ||
            workingDays.any { workingDay -> workingDay.fromTime >= workingDay.toTime }
        ) {
            throw InvalidCafeWorkingHoursException()
        }

        val cafeUuid = dataStoreRepo.cafeUuid.firstOrNull() ?: throw NoCafeException()
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        cafeRepo.patchCafe(
            cafeUuid = cafeUuid,
            updateCafe =
                UpdateCafe(
                    workingDays = workingDays,
                ),
            token = token,
        )
    }

    private companion object {
        val FULL_WEEK = (1..7).toSet()
    }
}
