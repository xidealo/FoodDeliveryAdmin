package com.bunbeauty.domain.feature.time

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCurrentTimeFlowUseCase @Inject constructor(
    private val timeService: TimeService
) {

    operator fun invoke(timeZoneOffset: Int, interval: Int): Flow<Time> = flow {
        while (true) {
            val currentTime = timeService.getCurrentTime(timeZoneOffset)
            emit(currentTime)
            delay((interval - currentTime.second) * 1_000L)
        }
    }
}
