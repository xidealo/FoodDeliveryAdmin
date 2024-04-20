package com.bunbeauty.domain.repo

import com.bunbeauty.domain.model.city.City

interface CityRepo {

    suspend fun getCityByUuid(companyUuid: String, cityUuid: String): City?

    fun clearCache()
}
