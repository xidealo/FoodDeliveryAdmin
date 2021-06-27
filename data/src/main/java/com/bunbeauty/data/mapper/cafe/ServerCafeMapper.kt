package com.bunbeauty.data.mapper.cafe

import com.bunbeauty.domain.model.cafe.CafeEntity
import com.bunbeauty.domain.model.cafe.server.*
import javax.inject.Inject

class ServerCafeMapper @Inject constructor() : IServerCafeMapper {

    override fun from(model: ServerCafe): CafeEntity {
        return CafeEntity(
            uuid = model.uuid,
            address = toString(model.address),
            latitude = model.cafeEntity.coordinate?.latitude ?: 0.0,
            longitude = model.cafeEntity.coordinate?.longitude ?: 0.0,
            fromTime = model.cafeEntity.fromTime,
            toTime = model.cafeEntity.toTime,
            phone = model.cafeEntity.phone,
            visible = model.cafeEntity.visible
        )
    }

    override fun to(model: CafeEntity): ServerCafe {
        return ServerCafe(
            uuid = model.uuid,
            address = toAddress(model.address),
            cafeEntity = ServerCafeEntity(
                coordinate = ServerCoordinate(
                    latitude = model.latitude,
                    longitude = model.longitude
                ),
                fromTime = model.fromTime,
                toTime = model.toTime,
                phone = model.phone,
                visible = model.visible
            )
        )
    }

    private fun toString(address: ServerCafeAddress): String {
        return address.city + ", " + address.street.name + ", " + address.house
    }

    private fun toAddress(address: String): ServerCafeAddress {
        val addressParts = address.split(", ")
        return ServerCafeAddress(
            city = addressParts[0],
            street = ServerCafeStreet(name = addressParts[1]),
            house = addressParts[2]
        )
    }
}