package com.bunbeauty.data.repository

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.extensions.dataOrNull
import com.bunbeauty.data.mapper.cafe.CafeMapper
import com.bunbeauty.data.model.server.cafe.PatchCafeServer
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.DeliveryZone
import com.bunbeauty.domain.model.cafe.UpdateCafe
import com.bunbeauty.domain.model.cafe.UpdateInfoDeliveryZone
import com.bunbeauty.domain.repo.CafeRepo
import common.ApiResult
import common.extension.getNullableResult
import common.extension.onError
import common.extension.onSuccess

class CafeRepository(
    private val foodDeliveryApi: FoodDeliveryApi,
    private val cafeMapper: CafeMapper,
) : CafeRepo {
    private var cafeCache: Cafe? = null
    private var deliveryZoneListCache: List<DeliveryZone>? = null

    override suspend fun getCafeByUuid(uuid: String): Cafe? =
        cafeCache ?: foodDeliveryApi.getCafeByUuid(uuid).getNullableResult { cafeServer ->
            val cafe = cafeMapper.toCafe(cafeServer = cafeServer)
            cafeCache = cafe
            cafe
        }

    override suspend fun updateCafeFromTime(
        cafeUuid: String,
        fromDaySeconds: Int,
        token: String,
    ): Cafe? {
        val cafe = getCafeByUuid(cafeUuid) ?: return null
        val patchCafe =
            cafeMapper.toPatchCafeServer(
                cafe.copy(fromTime = fromDaySeconds),
            )

        return updateCafe(cafeUuid, patchCafe, token)
    }

    override suspend fun updateCafeToTime(
        cafeUuid: String,
        toDaySeconds: Int,
        token: String,
    ): Cafe? {
        val cafe = getCafeByUuid(cafeUuid) ?: return null
        val patchCafe =
            cafeMapper.toPatchCafeServer(
                cafe.copy(toTime = toDaySeconds),
            )

        return updateCafe(cafeUuid, patchCafe, token)
    }

    override suspend fun patchCafe(
        cafeUuid: String,
        updateCafe: UpdateCafe,
        token: String,
    ) = when (
        val result =
            foodDeliveryApi.patchCafe(
                cafeUuid = cafeUuid,
                patchCafe = cafeMapper.toPatchCafe(updateCafe = updateCafe),
                token = token,
            )
    ) {
        is ApiResult.Success -> {
            cafeCache = cafeMapper.toCafe(result.data)
        }

        is ApiResult.Error -> {
            throw result.apiError
        }
    }

    override suspend fun getPositionDeliveryZone(
        cafeUuid: String,
        token: String,
    ): List<DeliveryZone> {
        deliveryZoneListCache?.let { cachedZones ->
            if (cachedZones.isNotEmpty()) {
                return cachedZones
            }
        }
        val result = foodDeliveryApi.getDeliveryZone(cafeUuid, token)
        var deliveryZones: List<DeliveryZone> = emptyList()

        result
            .onSuccess { response ->
                deliveryZones = cafeMapper.getDeliveryZonePoints(response)
                deliveryZoneListCache = deliveryZones
            }.onError { error ->
                error.message
            }

        return deliveryZones
    }

    override suspend fun getDeliveryZone(
        cafeUuid: String,
        zoneUuid: String,
        token: String,
    ): DeliveryZone? {
        val zone =
            deliveryZoneListCache?.find { zoneDelivery ->
                zoneDelivery.uuid == zoneUuid
            }
        return zone ?: getPositionDeliveryZone(
            cafeUuid = cafeUuid,
            token = token,
        ).find { foundZone ->
            foundZone.uuid == zoneUuid
        }
    }

    override suspend fun updateInfoDeliveryZone(
        cafeUuid: String,
        zoneUuid: String,
        token: String,
        updateInfoZone: UpdateInfoDeliveryZone,
    ) {
        val patchZone = cafeMapper.toPatchDeliveryZone(updateInfoZone)

        foodDeliveryApi
            .patchDeliveryZone(
                zoneUuid = zoneUuid,
                cafeUuid = cafeUuid,
                token = token,
                patchZone = patchZone,
            ).onSuccess {
                deliveryZoneListCache =
                    deliveryZoneListCache?.map { zone ->
                        if (zone.uuid == zoneUuid) {
                            zone.copy(
                                nameZone = updateInfoZone.name ?: zone.nameZone,
                                minOrderCost = updateInfoZone.minOrderCost ?: zone.minOrderCost,
                                normalDeliveryCost =
                                    updateInfoZone.normalDeliveryCost
                                        ?: zone.normalDeliveryCost,
                                forLowDeliveryCost =
                                    updateInfoZone.forLowDeliveryCost
                                        ?: zone.forLowDeliveryCost,
                            )
                        } else {
                            zone
                        }
                    }
            }.onError { error ->
                error.message
            }
    }

    override fun clearCache() {
        cafeCache = null
        deliveryZoneListCache = null
    }

    private suspend fun updateCafe(
        cafeUuid: String,
        patchCafe: PatchCafeServer,
        token: String,
    ): Cafe? {
        val patchedCafeServer =
            foodDeliveryApi
                .patchCafe(
                    cafeUuid = cafeUuid,
                    patchCafe = patchCafe,
                    token = token,
                ).dataOrNull() ?: return null

        val updatedCafe = cafeMapper.toCafe(patchedCafeServer)
        cafeCache = updatedCafe

        return updatedCafe
    }
}
