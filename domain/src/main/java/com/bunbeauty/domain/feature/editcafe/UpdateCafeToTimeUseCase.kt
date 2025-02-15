package com.bunbeauty.domain.feature.editcafe

import com.bunbeauty.domain.exception.DataUpdateFailedException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.util.datetime.DateTimeUtil
import java.time.LocalTime

class UpdateCafeToTimeUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val dateTimeUtil: DateTimeUtil,
    private val cafeRepo: CafeRepo
) {

    suspend operator fun invoke(cafeUuid: String, time: LocalTime) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()
        val daySeconds = dateTimeUtil.getDaySeconds(time)
        cafeRepo.updateCafeToTime(
            cafeUuid = cafeUuid,
            toDaySeconds = daySeconds,
            token = token
        ) ?: throw DataUpdateFailedException()
    }
}
