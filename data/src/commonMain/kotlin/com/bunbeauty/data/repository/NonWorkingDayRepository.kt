package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.data.extensions.map
import com.bunbeauty.data.mapper.nonworkingday.NonWorkingDayMapper
import com.bunbeauty.data.model.server.nonworkingday.PatchNonWorkingDayServer
import com.bunbeauty.domain.model.nonworkingday.NewNonWorkingDay
import com.bunbeauty.domain.model.nonworkingday.NonWorkingDay
import com.bunbeauty.domain.repo.NonWorkingDayRepo

class NonWorkingDayRepository(
    private val foodDeliveryApi: FoodDeliveryApi,
    private val nonWorkingDayMapper: NonWorkingDayMapper,
) : NonWorkingDayRepo {
    private var nonWorkingDayMapCache: MutableMap<String, List<NonWorkingDay>> = mutableMapOf()

    override suspend fun getNonWorkingDayListByCafeUuid(cafeUuid: String): List<NonWorkingDay> {
        val cache = nonWorkingDayMapCache[cafeUuid]
        return if (cache == null) {
            val nonWorkingDayList = getRemoteNonWorkingDayList(cafeUuid)
            if (nonWorkingDayList == null) {
                emptyList()
            } else {
                nonWorkingDayMapCache[cafeUuid] = nonWorkingDayList
                nonWorkingDayList
            }
        } else {
            cache
        }
    }

    override suspend fun saveNonWorkingDay(
        token: String,
        newNonWorkingDay: NewNonWorkingDay,
    ): NonWorkingDay? =
        foodDeliveryApi
            .postNonWorkingDay(
                token = token,
                postNonWorkingDay = nonWorkingDayMapper.toPostNonWorkingDayServer(newNonWorkingDay),
            ).dataOrNull()
            ?.let { nonWorkingDayServer ->
                val nonWorkingDay = nonWorkingDayMapper.toNonWorkingDay(nonWorkingDayServer)
                nonWorkingDayMapCache[nonWorkingDay.cafeUuid] =
                    nonWorkingDayMapCache[nonWorkingDay.cafeUuid].orEmpty() + nonWorkingDay

                nonWorkingDay
            }

    override suspend fun updateNonWorkingDay(
        token: String,
        uuid: String,
        isVisible: Boolean,
    ): NonWorkingDay? =
        foodDeliveryApi
            .patchNonWorkingDay(
                token = token,
                uuid = uuid,
                patchNonWorkingDay = PatchNonWorkingDayServer(isVisible = isVisible),
            ).dataOrNull()
            ?.let { nonWorkingDayServer ->
                val nonWorkingDay = nonWorkingDayMapper.toNonWorkingDay(nonWorkingDayServer)
                nonWorkingDayMapCache[nonWorkingDay.cafeUuid] = getUpdatedCache(nonWorkingDay)

                nonWorkingDay
            }

    override fun clearCache() {
        nonWorkingDayMapCache.clear()
    }

    private fun getUpdatedCache(nonWorkingDay: NonWorkingDay): List<NonWorkingDay> =
        nonWorkingDayMapCache[nonWorkingDay.cafeUuid]
            ?.map { cachedNonWorkingDay ->
                if (cachedNonWorkingDay.uuid == nonWorkingDay.uuid) {
                    nonWorkingDay
                } else {
                    cachedNonWorkingDay
                }
            }.orEmpty()

    private suspend fun getRemoteNonWorkingDayList(cafeUuid: String): List<NonWorkingDay>? =
        foodDeliveryApi
            .getNonWorkingDaysByCafeUuid(cafeUuid)
            .dataOrNull()
            ?.map(nonWorkingDayMapper::toNonWorkingDay)
}
