package com.bunbeauty.data.mapper.cafe

import com.bunbeauty.data.model.entity.CafeEntity
import com.bunbeauty.data.model.server.cafe.CafeServer
import com.bunbeauty.data.model.server.cafe.PatchCafeServer
import com.bunbeauty.domain.model.cafe.Cafe
import javax.inject.Inject

class CafeMapper @Inject constructor() {

    fun toCafe(cafeServer: CafeServer): Cafe {
        return cafeServer.run {
            Cafe(
                uuid = uuid,
                address = address,
                latitude = latitude,
                longitude = longitude,
                fromTime = fromTime,
                toTime = toTime,
                offset = offset,
                phone = phone,
                visible = isVisible,
                cityUuid = cityUuid,
            )
        }
    }

    fun toCafe(cafeEntity: CafeEntity): Cafe {
        return cafeEntity.run {
            Cafe(
                uuid = uuid,
                address = address,
                latitude = latitude,
                longitude = longitude,
                fromTime = fromTime,
                toTime = toTime,
                offset = offset,
                phone = phone,
                visible = visible,
                cityUuid = cityUuid
            )
        }
    }

    fun toCafeEntity(cafe: Cafe): CafeEntity {
        return cafe.run {
            CafeEntity(
                uuid = uuid,
                address = address,
                latitude = latitude,
                longitude = longitude,
                fromTime = fromTime,
                toTime = toTime,
                offset = offset,
                phone = phone,
                visible = visible,
                cityUuid = cityUuid
            )
        }
    }

    fun toCafeEntity(cafeServer: CafeServer): CafeEntity {
        return cafeServer.run {
            CafeEntity(
                uuid = uuid,
                address = address,
                latitude = latitude,
                longitude = longitude,
                fromTime = fromTime,
                toTime = toTime,
                offset = offset,
                phone = phone,
                visible = isVisible,
                cityUuid = cityUuid
            )
        }
    }

    fun toPatchCafeServer(cafe: Cafe): PatchCafeServer {
        return cafe.run {
            PatchCafeServer(
                address = address,
                latitude = latitude,
                longitude = longitude,
                fromTime = fromTime,
                toTime = toTime,
                phone = phone,
                isVisible = visible,
            )
        }
    }

}