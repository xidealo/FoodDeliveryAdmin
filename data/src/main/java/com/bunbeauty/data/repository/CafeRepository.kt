package com.bunbeauty.data.repository

import com.bunbeauty.common.extension.getNullableResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.dao.CafeDao
import com.bunbeauty.data.mapper.cafe.CafeMapper
import com.bunbeauty.data.mapper.cafe.IEntityCafeMapper
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.CafeRepo
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CafeRepository @Inject constructor(
    private val networkConnector: FoodDeliveryApi,
    private val entityCafeMapper: IEntityCafeMapper,
    private val cafeMapper: CafeMapper,
    private val cafeDao: CafeDao
): CafeRepo {

    var cafeListCache: List<Cafe>? = null

    override suspend fun getCafeByUuid(uuid: String): Cafe? =
        cafeDao.getCafeByUuid(uuid)?.let { cafeEntity ->
            entityCafeMapper.from(cafeEntity)
        }

    override suspend fun getCafeListByCityUuid(cityUuid: String): List<Cafe> = withContext(IO) {
        cafeDao.getCafeListByCityUuid(cityUuid).map { cafeEntity ->
            entityCafeMapper.from(cafeEntity)
        }
    }

    override suspend fun getCafeList(token: String, cityUuid: String): List<Cafe> {
        val cache = cafeListCache
        return if (cache == null) {
            val cafeList = getRemoteCafeList(token, cityUuid)
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

    suspend fun getRemoteCafeList(token: String, cityUuid: String): List<Cafe>? {
        return networkConnector.getCafeList(token, cityUuid)
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