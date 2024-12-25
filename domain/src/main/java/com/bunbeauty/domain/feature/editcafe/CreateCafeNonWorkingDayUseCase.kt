package com.bunbeauty.domain.feature.editcafe

import com.bunbeauty.domain.exception.DataNotFoundException
import com.bunbeauty.domain.exception.DataSavingFailedException
import com.bunbeauty.domain.exception.NoTokenException
import com.bunbeauty.domain.model.nonworkingday.NewNonWorkingDay
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.NonWorkingDayRepo
import java.time.LocalDate
import java.time.ZoneOffset

class CreateCafeNonWorkingDayUseCase (
    private val cafeRepo: CafeRepo,
    private val nonWorkingDayRepo: NonWorkingDayRepo,
    private val dataStoreRepo: DataStoreRepo
) {

    suspend operator fun invoke(date: LocalDate, cafeUuid: String) {
        val token = dataStoreRepo.getToken() ?: throw NoTokenException()

        val cafe = cafeRepo.getCafeByUuid(cafeUuid) ?: throw DataNotFoundException()
        val cafeZoneOffset = ZoneOffset.ofHours(cafe.offset)
        val timestamp = date.atStartOfDay().toEpochSecond(cafeZoneOffset) * 1_000

        nonWorkingDayRepo.saveNonWorkingDay(
            token = token,
            newNonWorkingDay = NewNonWorkingDay(
                timestamp = timestamp,
                cafeUuid = cafeUuid
            )
        ) ?: throw DataSavingFailedException()
    }
}
