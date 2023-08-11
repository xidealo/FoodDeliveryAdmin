package com.bunbeauty.domain.feature.cafelist

import com.bunbeauty.domain.exception.DataNotFoundException
import com.bunbeauty.domain.feature.orders.GetCafeListUseCase
import com.bunbeauty.domain.feature.time.GetCurrentTimeFlowUseCase
import com.bunbeauty.domain.model.cafe.CafeStatus
import com.bunbeauty.domain.model.cafe.CafeWithWorkingHours
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val SECONDS_IN_MINUTE = 60
private const val SECONDS_IN_HOUR = 60 * 60
private const val SECONDS_IN_DAY = 24 * 60 * 60

class GetCafeWithWorkingHoursListFlowUseCase @Inject constructor(
    private val dataStoreRepo: DataStoreRepo,
    private val getTimeZoneByCityUuid: GetTimeZoneByCityUuidUseCase,
    private val getCafeList: GetCafeListUseCase,
    private val getCurrentTimeFlow: GetCurrentTimeFlowUseCase,
) {

    suspend operator fun invoke(): Flow<List<CafeWithWorkingHours>> {
        val cityUuid = dataStoreRepo.managerCity.firstOrNull() ?: throw DataNotFoundException()
        val timeZone = getTimeZoneByCityUuid(cityUuid)
        val cafeList = getCafeList()

        return getCurrentTimeFlow(timeZone = timeZone, interval = SECONDS_IN_MINUTE).map { currentTime ->
            val currentDaySeconds = currentTime.minute * SECONDS_IN_MINUTE + currentTime.hour * SECONDS_IN_HOUR
            cafeList.map { cafe ->
                CafeWithWorkingHours(
                    uuid = cafe.uuid,
                    address = cafe.address,
                    workingHours = "${getCafeTime(cafe.fromTime)} - ${getCafeTime(cafe.toTime)}",
                    status = getStatus(cafe.fromTime, cafe.toTime, currentDaySeconds),
                    cityUuid = cafe.cityUuid,
                )
            }
        }
    }

    private fun getCafeTime(daySeconds: Int): String {
        val hours = daySeconds / SECONDS_IN_HOUR
        val minutes = (daySeconds % SECONDS_IN_HOUR) / SECONDS_IN_MINUTE
        val minutesString = if (minutes < 10) {
            "0$minutes"
        } else {
            minutes.toString()
        }

        return "$hours:$minutesString"
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