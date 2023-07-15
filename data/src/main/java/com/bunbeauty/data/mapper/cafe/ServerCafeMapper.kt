package com.bunbeauty.data.mapper.cafe

import com.bunbeauty.data.model.entity.CafeEntity
import com.bunbeauty.data.model.server.cafe.*
import javax.inject.Inject

class ServerCafeMapper @Inject constructor() : IServerCafeMapper {

    override fun from(model: CafeServer): CafeEntity {
        return CafeEntity(
            uuid = model.uuid,
            address = model.address,
            latitude = model.latitude,
            longitude = model.longitude,
            fromTime = model.fromTime.toString(),
            toTime = model.toTime.toString(),
            phone = model.phone,
            visible = model.isVisible,
            cityUuid = model.cityUuid
        )
    }

    override fun to(model: CafeEntity): CafeServer {
        return CafeServer(
            uuid = model.uuid,
            address = model.address,
            latitude = model.latitude,
            longitude = model.longitude,
            fromTime = model.fromTime.toInt(),
            toTime = model.toTime.toInt(),
            phone = model.phone,
            cityUuid = model.cityUuid,
            isVisible = model.visible
        )
    }

}