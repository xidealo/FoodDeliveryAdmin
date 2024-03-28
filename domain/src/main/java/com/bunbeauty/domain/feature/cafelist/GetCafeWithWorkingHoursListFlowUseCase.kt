package com.bunbeauty.domain.feature.cafelist

import com.bunbeauty.domain.feature.common.GetCafeListUseCase
import com.bunbeauty.domain.feature.time.GetCurrentTimeFlowUseCase
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.CafeStatus
import com.bunbeauty.domain.model.cafe.CafeWithWorkingHours
import com.bunbeauty.domain.util.datetime.IDateTimeUtil
import com.bunbeauty.domain.util.flattenFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val SECONDS_IN_MINUTE = 60
private const val SECONDS_IN_HOUR = 60 * 60
private const val SECONDS_IN_DAY = 24 * 60 * 60

class GetCafeWithWorkingHoursListFlowUseCase @Inject constructor(
    private val getCafeList: GetCafeListUseCase,
    private val getCurrentTimeFlow: GetCurrentTimeFlowUseCase,
    private val dateTimeUtil: IDateTimeUtil,
) {

    suspend operator fun invoke(): Flow<List<CafeWithWorkingHours>> {
        return getCafeList().map { cafe ->
            getCurrentTimeFlow(
                timeZoneOffset = cafe.offset,
                interval = SECONDS_IN_MINUTE
            ).map { time ->
                val currentDaySeconds = time.minute * SECONDS_IN_MINUTE + time.hour * SECONDS_IN_HOUR
                CafeWithWorkingHours(
                    uuid = cafe.uuid,
                    address = cafe.address,
                    workingHours = getWorkingHours(cafe),
                    status = getStatus(cafe.fromTime, cafe.toTime, currentDaySeconds),
                    cityUuid = cafe.cityUuid,
                )
            }
        }.flattenFlow()
    }

    private fun getWorkingHours(cafe: Cafe): String {
        val fromTimeText = dateTimeUtil.getTimeHHMM(cafe.fromTime)
        val toTimeText = dateTimeUtil.getTimeHHMM(cafe.toTime)

        return "$fromTimeText - $toTimeText"
    }

    private fun getStatus(fromDaySeconds: Int, toDaySeconds: Int, currentDaySeconds: Int): CafeStatus {
        return if (fromDaySeconds < toDaySeconds) {
            if (currentDaySeconds in fromDaySeconds until toDaySeconds) {
                val closeIn = toDaySeconds - currentDaySeconds
                if (closeIn < SECONDS_IN_HOUR) {
                    CafeStatus.CloseSoon(closeIn / SECONDS_IN_MINUTE)
                } else {
                    CafeStatus.Open
                }
            } else {
                CafeStatus.Closed
            }
        } else {
            if (currentDaySeconds in toDaySeconds until fromDaySeconds) {
                CafeStatus.Closed
            } else {
                val closeIn = if (toDaySeconds >= currentDaySeconds) {
                    toDaySeconds - currentDaySeconds
                } else {
                    toDaySeconds + SECONDS_IN_DAY - currentDaySeconds
                }
                if (closeIn < SECONDS_IN_HOUR) {
                    CafeStatus.CloseSoon(closeIn / SECONDS_IN_MINUTE)
                } else {
                    CafeStatus.Open
                }
            }
        }
    }

}