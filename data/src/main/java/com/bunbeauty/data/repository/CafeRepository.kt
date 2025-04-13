package com.bunbeauty.data.repository

import com.bunbeauty.common.ApiResult
import com.bunbeauty.common.extension.getNullableResult
import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.data.mapper.cafe.CafeMapper
import com.bunbeauty.data.model.server.cafe.PatchCafeServer
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.UpdateCafe
import com.bunbeauty.domain.repo.CafeRepo

class CafeRepository(
    private val foodDeliveryApi: FoodDeliveryApi,
    private val cafeMapper: CafeMapper
) : CafeRepo {

    private var cafeCache: Cafe? = null

    override suspend fun getCafeByUuid(uuid: String): Cafe? {
        return cafeCache ?: foodDeliveryApi.getCafeByUuid(uuid).getNullableResult { cafeServer ->
            val cafe = cafeMapper.toCafe(cafeServer = cafeServer)
            cafeCache = cafe
            cafe
        }
    }

    override suspend fun updateCafeFromTime(
        cafeUuid: String,
        fromDaySeconds: Int,
        token: String
    ): Cafe? {
        val cafe = getCafeByUuid(cafeUuid) ?: return null
        val patchCafe = cafeMapper.toPatchCafeServer(
            cafe.copy(fromTime = fromDaySeconds)
        )

        return updateCafe(cafeUuid, patchCafe, token)
    }

    override suspend fun updateCafeToTime(
        cafeUuid: String,
        toDaySeconds: Int,
        token: String
    ): Cafe? {
        val cafe = getCafeByUuid(cafeUuid) ?: return null
        val patchCafe = cafeMapper.toPatchCafeServer(
            cafe.copy(toTime = toDaySeconds)
        )

        return updateCafe(cafeUuid, patchCafe, token)
    }

    override suspend fun updateWorkCafe(
        updateCafe: UpdateCafe,
        cafeUuid: String,
        token: String
    ) {
        return when (
            val result = foodDeliveryApi.patchCafe(
                cafeUuid = cafeUuid,
                patchCafe = cafeMapper.toPatchCafe(updateCafe = updateCafe),
                token = token
            )
        ) {
            is ApiResult.Success -> {
                cafeCache = cafeMapper.toCafe(result.data)
            }

            is ApiResult.Error -> {
                throw result.apiError
            }
        }
    }

    override suspend fun patchCafe(
        cafeUuid: String,
        updateCafe: UpdateCafe,
        token: String
    ) {
        return when (
            val result = foodDeliveryApi.patchCafe(
                cafeUuid = cafeUuid,
                patchCafe = cafeMapper.toPatchCafe(updateCafe = updateCafe),
                token = token
            )
        ) {
            is ApiResult.Success -> {
                cafeCache = cafeMapper.toCafe(result.data)
            }

            is ApiResult.Error -> {
                throw result.apiError
            }
        }
    }

    override fun clearCache() {
        cafeCache = null
    }

    private suspend fun updateCafe(
        cafeUuid: String,
        patchCafe: PatchCafeServer,
        token: String
    ): Cafe? {
        val patchedCafeServer = foodDeliveryApi.patchCafe(
            cafeUuid = cafeUuid,
            patchCafe = patchCafe,
            token = token
        ).dataOrNull() ?: return null

        val updatedCafe = cafeMapper.toCafe(patchedCafeServer)
        cafeCache = updatedCafe

        return updatedCafe
    }
}
