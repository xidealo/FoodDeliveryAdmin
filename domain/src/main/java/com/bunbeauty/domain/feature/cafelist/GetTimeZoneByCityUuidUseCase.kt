package com.bunbeauty.domain.feature.cafelist

import com.bunbeauty.domain.repo.CityRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import kotlinx.coroutines.flow.firstOrNull

private const val DEFAULT_TIME_ZONE = "UTC+3"

class GetTimeZoneByCityUuidUseCase(
    private val dataStoreRepo: DataStoreRepo,
    private val cityRepo: CityRepo
) {

    suspend operator fun invoke(cityUuid: String): String {
        val companyUuid = dataStoreRepo.companyUuid.firstOrNull() ?: return DEFAULT_TIME_ZONE

        return cityRepo.getCityByUuid(
            companyUuid = companyUuid,
            cityUuid = cityUuid
        )?.timeZone ?: DEFAULT_TIME_ZONE
    }
}
