package com.bunbeauty.data.mapper.cafe

import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.data.model.entity.CafeEntity
import javax.inject.Inject

class EntityCafeMapper @Inject constructor(): IEntityCafeMapper {

    override fun from(model: CafeEntity): Cafe {
        return Cafe(
            uuid = model.uuid,
            address = model.address,
            latitude = model.latitude,
            longitude = model.longitude,
            fromTime = model.fromTime,
            toTime = model.toTime,
            phone = model.phone,
            visible = model.visible,
        )
    }

    override fun to(model: Cafe): CafeEntity {
        return CafeEntity(
            uuid = model.uuid,
            address = model.address,
            latitude = model.latitude,
            longitude = model.longitude,
            fromTime = model.fromTime,
            toTime = model.toTime,
            phone = model.phone,
            visible = model.visible,
        )
    }
}