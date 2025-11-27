package com.bunbeauty.domain.feature.time

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCurrentTimeMillisFlowUseCase(
    private val timeService: TimeService,
) {
    operator fun invoke(intervalInMillis: Int): Flow<Long> =
        flow {
            while (true) {
                val currentTimeMillis = timeService.getCurrentTimeMillis()
                emit(currentTimeMillis)
                delay(intervalInMillis - currentTimeMillis % intervalInMillis)
            }
        }
}
