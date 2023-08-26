package com.bunbeauty.data.mapper.cafe

import com.bunbeauty.data.model.entity.CafeEntity
import com.bunbeauty.data.model.server.cafe.CafeServer
import com.bunbeauty.domain.model.cafe.Cafe
import javax.inject.Inject

class CafeMapper @Inject constructor() {

    fun map(cafeServer: CafeServer): Cafe {
        return Cafe(
            uuid = cafeServer.uuid,
            address = cafeServer.address,
            latitude = cafeServer.latitude,
            longitude = cafeServer.longitude,
            fromTime = cafeServer.fromTime,
            toTime = cafeServer.toTime,
            phone = cafeServer.phone,
            visible = cafeServer.isVisible,
            cityUuid = cafeServer.cityUuid
        )
    }

    fun map(cafeEntity: CafeEntity): Cafe {
        return Cafe(
            uuid = cafeEntity.uuid,
            address = cafeEntity.address,
            latitude = cafeEntity.latitude,
            longitude = cafeEntity.longitude,
            fromTime = cafeEntity.fromTime.toIntOrNull() ?: 0,
            toTime = cafeEntity.toTime.toIntOrNull() ?: 0,
            phone = cafeEntity.phone,
            visible = cafeEntity.visible,
            cityUuid = cafeEntity.cityUuid
        )
    }

    fun map(cafe: Cafe): CafeEntity {
        return CafeEntity(
            uuid = cafe.uuid,
            address = cafe.address,
            latitude = cafe.latitude,
            longitude = cafe.longitude,
            fromTime = cafe.fromTime.toString(),
            toTime = cafe.toTime.toString(),
            phone = cafe.phone,
            visible = cafe.visible,
            cityUuid = cafe.cityUuid
        )
    }
}