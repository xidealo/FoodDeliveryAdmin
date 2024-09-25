package com.bunbeauty.domain.feature.main

import com.bunbeauty.domain.feature.time.GetCurrentTimeMillisFlowUseCase
import com.bunbeauty.domain.repo.NonWorkingDayRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val ONE_MINUTE_INTERVAL = 60 * 1_000
private const val MILLIS_IN_ONE_DAY = 24 * 60 * 60 * 1_000

class GetIsNonWorkingDayFlowUseCase @Inject constructor(
    private val getSelectedCafeFlow: GetSelectedCafeFlowUseCase,
    private val getCurrentTimeMillisFlow: GetCurrentTimeMillisFlowUseCase,
    private val nonWorkingDayRepo: NonWorkingDayRepo
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Boolean> {
        return getSelectedCafeFlow()
            .filterNotNull()
            .flatMapLatest { selectedCafe ->
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
