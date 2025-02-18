package com.bunbeauty.domain.feature.main

import com.bunbeauty.domain.feature.common.GetCafeUseCase
import com.bunbeauty.domain.feature.time.GetCurrentTimeMillisFlowUseCase
import com.bunbeauty.domain.repo.NonWorkingDayRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val ONE_MINUTE_INTERVAL = 60 * 1_000
private const val MILLIS_IN_ONE_DAY = 24 * 60 * 60 * 1_000

class GetIsNonWorkingDayFlowUseCase(
    private val getCafeUseCase: GetCafeUseCase,
    private val getCurrentTimeMillisFlow: GetCurrentTimeMillisFlowUseCase,
    private val nonWorkingDayRepo: NonWorkingDayRepo
) {

    suspend operator fun invoke(): Flow<Boolean> {
        return getCafeUseCase().let { selectedCafe ->
            val cafeUuid = selectedCafe.uuid

            getCurrentTimeMillisFlow(intervalInMillis = ONE_MINUTE_INTERVAL).map { timeMillis ->
                val nonWorkingDayList = nonWorkingDayRepo.getNonWorkingDayListByCafeUuid(cafeUuid)
                    .filter { nonWorkingDay ->
                        nonWorkingDay.isVisible
                    }
                nonWorkingDayList.any { nonWorkingDay ->
                    timeMillis - nonWorkingDay.timestamp in 0..MILLIS_IN_ONE_DAY
                }
            }
        }
    }
}
