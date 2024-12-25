package com.bunbeauty.data.repository

import com.bunbeauty.common.extension.getNullableResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.dao.CafeDao
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.data.mapper.cafe.CafeMapper
import com.bunbeauty.data.model.server.cafe.PatchCafeServer
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.repo.CafeRepo


class CafeRepository (
    private val foodDeliveryApi: FoodDeliveryApi,
    private val cafeMapper: CafeMapper,
    private val cafeDao: CafeDao
) : CafeRepo {

    private var cafeListCache: List<Cafe>? = null

    override suspend fun getCafeByUuid(uuid: String): Cafe? {
        val cachedCafe = cafeListCache?.find { cafe ->
            cafe.uuid == uuid
        }

        return cachedCafe ?: cafeDao.getCafeByUuid(uuid)?.let(cafeMapper::toCafe)
    }

    override suspend fun getCafeListByCityUuid(cityUuid: String): List<Cafe> {
        return cafeDao.getCafeListByCityUuid(cityUuid).map(cafeMapper::toCafe)
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

    override suspend fun updateCafeFromTime(cafeUuid: String, fromDaySeconds: Int, token: String): Cafe? {
        val cafe = getCafeByUuid(cafeUuid) ?: return null
        val patchCafe = cafeMapper.toPatchCafeServer(
            cafe.copy(fromTime = fromDaySeconds)
        )

        return updateCafe(cafeUuid, patchCafe, token)
    }

    override suspend fun updateCafeToTime(cafeUuid: String, toDaySeconds: Int, token: String): Cafe? {
        val cafe = getCafeByUuid(cafeUuid) ?: return null
        val patchCafe = cafeMapper.toPatchCafeServer(
            cafe.copy(toTime = toDaySeconds)
        )

        return updateCafe(cafeUuid, patchCafe, token)
    }

    override fun clearCache() {
        cafeListCache = null
    }

    private suspend fun getRemoteCafeList(cityUuid: String): List<Cafe>? {
        return foodDeliveryApi.getCafeList(cityUuid)
            .getNullableResult { cafeServerList ->
                cafeServerList.results.map(cafeMapper::toCafe)
            }
    }

    private suspend fun getLocalCafeList(cityUuid: String): List<Cafe> {
        return cafeDao.getCafeListByCityUuid(cityUuid).map(cafeMapper::toCafe)
    }

    private suspend fun saveCafeListLocally(cafeList: List<Cafe>) {
        cafeDao.insertAll(
            cafeList.map(cafeMapper::toCafeEntity)
        )
    }

    private suspend fun updateCafe(cafeUuid: String, patchCafe: PatchCafeServer, token: String): Cafe? {
        val patchedCafeServer = foodDeliveryApi.patchCafe(
            cafeUuid = cafeUuid,
            patchCafe = patchCafe,
            token = token
        ).dataOrNull() ?: return null

        cafeDao.insert(cafeMapper.toCafeEntity(patchedCafeServer))

        val updatedCafe = cafeMapper.toCafe(patchedCafeServer)
        cafeListCache = getUpdatedCache(cafe = updatedCafe)

        return updatedCafe
    }

    private fun getUpdatedCache(cafe: Cafe): List<Cafe>? {
        return cafeListCache?.map { cachedCafe ->
            if (cachedCafe.uuid == cafe.uuid) {
                cafe
            } else {
                cachedCafe
            }
        }
    }
}
