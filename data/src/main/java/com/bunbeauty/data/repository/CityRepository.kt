package com.bunbeauty.data.repository

import com.bunbeauty.common.extension.getNullableResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.mapper.city.CityMapper
import com.bunbeauty.domain.model.city.City
import com.bunbeauty.domain.repo.CityRepo

class CityRepository(
    private val foodDeliveryApi: FoodDeliveryApi,
    private val cityMapper: CityMapper,
) : CityRepo {
    var cityListCache: List<City>? = null

    override suspend fun getCityByUuid(
        companyUuid: String,
        cityUuid: String,
    ): City? =
        getCityList(companyUuid).find { city ->
            city.uuid == cityUuid
        }

    override fun clearCache() {
        cityListCache = null
    }

    suspend fun getCityList(companyUuid: String): List<City> {
        val cache = cityListCache
        return if (cache == null) {
            val cityList = getRemoteCityList(companyUuid)
            if (cityList == null) {
                emptyList()
            } else {
                cityListCache = cityList
                cityList
            }
        } else {
            cache
        }
    }

    suspend fun getRemoteCityList(companyUuid: String): List<City>? =
        foodDeliveryApi
            .getCityList(companyUuid)
            .getNullableResult { cityServerList ->
                cityServerList.results.map(cityMapper::map)
            }
}
