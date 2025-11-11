package com.bunbeauty.data.mapper.city

import com.bunbeauty.data.model.entity.CityEntity
import com.bunbeauty.data.model.server.city.CityServer
import com.bunbeauty.domain.model.city.City

class CityMapper {
    fun map(city: City): CityEntity =
        CityEntity(
            uuid = city.uuid,
            name = city.name,
            timeZone = city.timeZone,
            isVisible = city.isVisible,
        )

    fun map(cityServer: CityServer): City =
        City(
            uuid = cityServer.uuid,
            name = cityServer.name,
            timeZone = cityServer.timeZone,
            isVisible = cityServer.isVisible,
        )

    fun map(cityEntity: CityEntity): City =
        City(
            uuid = cityEntity.uuid,
            name = cityEntity.name,
            timeZone = cityEntity.timeZone,
            isVisible = cityEntity.isVisible,
        )
}
