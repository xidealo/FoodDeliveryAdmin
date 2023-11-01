package com.bunbeauty.data.repository

import com.bunbeauty.common.extension.getNullableResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.dao.CafeDao
import com.bunbeauty.data.mapper.cafe.CafeMapper
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.CafeRepo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CafeRepository @Inject constructor(
    private val foodDeliveryApi: FoodDeliveryApi,
    private val cafeMapper: CafeMapper,
    private val cafeDao: CafeDao
): CafeRepo {

    var cafeListCache: List<Cafe>? = null

    override suspend fun getCafeByUuid(uuid: String): Cafe? =
        cafeDao.getCafeByUuid(uuid)?.let(cafeMapper::map)

    override suspend fun getCafeListByCityUuid(cityUuid: String): List<Cafe> = withContext(IO) {
        cafeDao.getCafeListByCityUuid(cityUuid).map(cafeMapper::map)
    }

    override suspend fun getCafeList(cityUuid: String): List<Cafe> {
        val cache = cafeListCache
        return if (cache == null) {
            val cafeList = getRemoteCafeList(cityUuid)
            if (cafeList == null) {
                getLocalCafeList(cityUuid)
            } else {
                saveCafeListLocally(cafeList)
                cafeListCache = cafeList
                cafeList
            }
        } else {
            cache
        }
    }

    override fun clearCache() {
        cafeListCache = null
    }

    suspend fun getRemoteCafeList(cityUuid: String): List<Cafe>? {
        return foodDeliveryApi.getCafeList(cityUuid)
            .getNullableResult { cafeServerList ->
                cafeServerList.results.map(cafeMapper::map)
            }
    }

    suspend fun getLocalCafeList(cityUuid: String): List<Cafe> {
        return cafeDao.getCafeListByCityUuid(cityUuid).map(cafeMapper::map)
    }

    suspend fun saveCafeListLocally(cafeList: List<Cafe>) {
        cafeDao.insertAll(
            cafeList.map(cafeMapper::map)
        )
    }
}