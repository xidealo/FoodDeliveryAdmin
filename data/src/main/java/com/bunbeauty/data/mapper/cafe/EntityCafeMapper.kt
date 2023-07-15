package com.bunbeauty.data.mapper.cafe

import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.data.model.entity.CafeEntity
import javax.inject.Inject

class EntityCafeMapper @Inject constructor() : IEntityCafeMapper {

    override fun from(model: CafeEntity): Cafe {
        return Cafe(
            uuid = model.uuid,
            address = model.address,
            latitude = model.latitude,
            longitude = model.longitude,
            fromTime = model.fromTime.toIntOrNull() ?: 0,
            toTime = model.toTime.toIntOrNull() ?: 0,
            phone = model.phone,
            visible = model.visible,
            cityUuid = model.cityUuid
        )
    }

    override fun to(model: Cafe): CafeEntity {
        return CafeEntity(
            uuid = model.uuid,
            address = model.address,
            latitude = model.latitude,
            longitude = model.longitude,
            fromTime = model.fromTime.toString(),
            toTime = model.toTime.toString(),
            phone = model.phone,
            visible = model.visible,
            cityUuid = model.cityUuid
        )
    }
}