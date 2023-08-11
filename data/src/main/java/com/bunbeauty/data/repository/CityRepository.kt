package com.bunbeauty.data.repository

import com.bunbeauty.common.extension.getNullableResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.dao.CityDao
import com.bunbeauty.data.mapper.city.CityMapper
import com.bunbeauty.domain.model.city.City
import com.bunbeauty.domain.repo.CityRepo
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val foodDeliveryApi: FoodDeliveryApi,
    private val cityDao: CityDao,
    private val cityMapper: CityMapper,
) : CityRepo {

    var cityListCache: List<City>? = null

    override suspend fun getCityByUuid(companyUuid: String, cityUuid: String): City? {
        return getCityList(companyUuid).find { city ->
            city.uuid == cityUuid
        }
    }

    override fun clearCache() {
        cityListCache = null
    }

    suspend fun getCityList(companyUuid: String): List<City> {
        val cache = cityListCache
        return if (cache == null) {
            val cityList = getRemoteCityList(companyUuid)
            if (cityList == null) {
                getLocalCityList()
            } else {
                saveCityListLocally(cityList)
                cityListCache = cityList
                cityList
            }
        } else {
            cache
        }
    }

    suspend fun getRemoteCityList(companyUuid: String): List<City>? {
        return foodDeliveryApi.getCityList(companyUuid)
            .getNullableResult { cityServerList ->
                cityServerList.results.map(cityMapper::map)
            }
    }

    suspend fun getLocalCityList(): List<City> {
        return cityDao.getCityList().map(cityMapper::map)
    }

    suspend fun saveCityListLocally(cityList: List<City>) {
        cityDao.insertAll(
            cityList.map(cityMapper::map)
        )
    }

}