package com.bunbeauty.domain.feature.editcafe

import com.bunbeauty.domain.exception.DataNotFoundException
import com.bunbeauty.domain.feature.time.TimeService
import com.bunbeauty.domain.model.nonworkingday.FormattedNonWorkingDay
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.NonWorkingDayRepo
import com.bunbeauty.domain.util.datetime.IDateTimeUtil
import com.bunbeauty.domain.util.datetime.PATTERN_DD_MMMM
import javax.inject.Inject

class GetNonWorkingDayListByCafeUuidUseCase @Inject constructor(
    private val cafeRepo: CafeRepo,
    private val nonWorkingDayRepo: NonWorkingDayRepo,
    private val dateTimeUtil: IDateTimeUtil,
    private val timeService: TimeService
) {

    suspend operator fun invoke(cafeUuid: String): List<FormattedNonWorkingDay> {
        val cafe = cafeRepo.getCafeByUuid(cafeUuid) ?: throw DataNotFoundException()
        val currentDayStartMillis = timeService.getCurrentDayStartMillis(cafe.offset)
        return nonWorkingDayRepo.getNonWorkingDayListByCafeUuid(cafeUuid)
            .filter { nonWorkingDay ->
                (nonWorkingDay.timestamp >= currentDayStartMillis) &&
                    nonWorkingDay.isVisible
            }
            .sortedBy { nonWorkingDay ->
                nonWorkingDay.timestamp
            }.map { nonWorkingDay ->
                FormattedNonWorkingDay(
                    uuid = nonWorkingDay.uuid,
                    date = dateTimeUtil.formatDateTime(
                        millis = nonWorkingDay.timestamp,
                        pattern = PATTERN_DD_MMMM,
                        offset = cafe.offset
                    ),
                    cafeUuid = nonWorkingDay.cafeUuid
                )
            }
    }
}
