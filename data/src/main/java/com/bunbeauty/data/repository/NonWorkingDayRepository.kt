package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.dao.NonWorkingDayDao
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.data.extensions.map
import com.bunbeauty.data.mapper.nonworkingday.NonWorkingDayMapper
import com.bunbeauty.data.model.server.nonworkingday.PatchNonWorkingDayServer
import com.bunbeauty.domain.model.nonworkingday.NewNonWorkingDay
import com.bunbeauty.domain.model.nonworkingday.NonWorkingDay
import com.bunbeauty.domain.repo.NonWorkingDayRepo
import javax.inject.Inject

class NonWorkingDayRepository @Inject constructor(
    private val foodDeliveryApi: FoodDeliveryApi,
    private val nonWorkingDayDao: NonWorkingDayDao,
    private val nonWorkingDayMapper: NonWorkingDayMapper
) : NonWorkingDayRepo {

    private var nonWorkingDayMapCache: MutableMap<String, List<NonWorkingDay>> = mutableMapOf()

    override suspend fun getNonWorkingDayListByCafeUuid(cafeUuid: String): List<NonWorkingDay> {
        val cache = nonWorkingDayMapCache[cafeUuid]
        return if (cache == null) {
            val nonWorkingDayList = getRemoteNonWorkingDayList(cafeUuid)
            if (nonWorkingDayList == null) {
                getLocalNonWorkingDayList(cafeUuid)
            } else {
                saveNonWorkingDayListLocally(nonWorkingDayList)
                nonWorkingDayMapCache[cafeUuid] = nonWorkingDayList
                nonWorkingDayList
            }
        } else {
            cache
        }
    }

    override suspend fun saveNonWorkingDay(token: String, newNonWorkingDay: NewNonWorkingDay): NonWorkingDay? {
        return foodDeliveryApi.postNonWorkingDay(
            token = token,
            postNonWorkingDay = nonWorkingDayMapper.toPostNonWorkingDayServer(newNonWorkingDay)
        ).dataOrNull()?.let { nonWorkingDayServer ->
            val nonWorkingDay = nonWorkingDayMapper.toNonWorkingDay(nonWorkingDayServer)
            nonWorkingDayDao.insert(nonWorkingDayMapper.toNonWorkingDayEntity(nonWorkingDay))
            nonWorkingDayMapCache[nonWorkingDay.cafeUuid] =
                nonWorkingDayMapCache[nonWorkingDay.cafeUuid].orEmpty() + nonWorkingDay

            nonWorkingDay
        }
    }

    override suspend fun updateNonWorkingDay(token: String, uuid: String, isVisible: Boolean): NonWorkingDay? {
        return foodDeliveryApi.patchNonWorkingDay(
            token = token,
            uuid = uuid,
            patchNonWorkingDay = PatchNonWorkingDayServer(isVisible = isVisible)
        ).dataOrNull()?.let { nonWorkingDayServer ->
            val nonWorkingDay = nonWorkingDayMapper.toNonWorkingDay(nonWorkingDayServer)
            nonWorkingDayDao.insert(nonWorkingDayMapper.toNonWorkingDayEntity(nonWorkingDay))
            nonWorkingDayMapCache[nonWorkingDay.cafeUuid] = getUpdatedCache(nonWorkingDay)

            nonWorkingDay
        }
    }

    override fun clearCache() {
        nonWorkingDayMapCache.clear()
    }

    private fun getUpdatedCache(nonWorkingDay: NonWorkingDay): List<NonWorkingDay> {
        return nonWorkingDayMapCache[nonWorkingDay.cafeUuid]?.map { cachedNonWorkingDay ->
            if (cachedNonWorkingDay.uuid == nonWorkingDay.uuid) {
                nonWorkingDay
            } else {
                cachedNonWorkingDay
            }
        }.orEmpty()
    }

    private suspend fun getRemoteNonWorkingDayList(cafeUuid: String): List<NonWorkingDay>? {
        return foodDeliveryApi.getNonWorkingDaysByCafeUuid(cafeUuid).dataOrNull()
            ?.map(nonWorkingDayMapper::toNonWorkingDay)
    }

    private suspend fun getLocalNonWorkingDayList(cafeUuid: String): List<NonWorkingDay> {
        return nonWorkingDayDao.getNonWorkingDayListByCafeUuid(cafeUuid).map(nonWorkingDayMapper::toNonWorkingDay)
    }

    private suspend fun saveNonWorkingDayListLocally(nonWorkingDayList: List<NonWorkingDay>) {
        nonWorkingDayDao.insertAll(
            nonWorkingDayList.map(nonWorkingDayMapper::toNonWorkingDayEntity)
        )
    }
}
