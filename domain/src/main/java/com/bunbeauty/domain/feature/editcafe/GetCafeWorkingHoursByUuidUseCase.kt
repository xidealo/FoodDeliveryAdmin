package com.bunbeauty.domain.feature.editcafe

import com.bunbeauty.domain.exception.DataNotFoundException
import com.bunbeauty.domain.model.cafe.CafeWorkingHours
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.util.datetime.IDateTimeUtil

class GetCafeWorkingHoursByUuidUseCase(
    private val cafeRepo: CafeRepo,
    private val dateTimeUtil: IDateTimeUtil
) {

    suspend operator fun invoke(uuid: String): CafeWorkingHours {
        val cafe = cafeRepo.getCafeByUuid(uuid) ?: throw DataNotFoundException()
        return CafeWorkingHours(
            fromTimeText = dateTimeUtil.getTimeHHMM(cafe.fromTime),
            fromTime = dateTimeUtil.getLocalTime(cafe.fromTime),
            toTimeText = dateTimeUtil.getTimeHHMM(cafe.toTime),
            toTime = dateTimeUtil.getLocalTime(cafe.toTime)
        )
    }
}
