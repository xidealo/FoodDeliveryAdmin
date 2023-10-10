package com.bunbeauty.data.mapper.city

import com.bunbeauty.data.model.entity.CityEntity
import com.bunbeauty.data.model.server.city.CityServer
import com.bunbeauty.domain.model.city.City
import javax.inject.Inject

class CityMapper @Inject constructor() {

    fun map(city: City): CityEntity {
        return CityEntity(
            uuid = city.uuid,
            name = city.name,
            timeZone = city.timeZone,
            isVisible = city.isVisible
        )
    }

    fun map(cityServer: CityServer): City {
        return City(
            uuid = cityServer.uuid,
            name = cityServer.name,
            timeZone = cityServer.timeZone,
            isVisible = cityServer.isVisible
        )
    }

    fun map(cityEntity: CityEntity): City {
        return City(
            uuid = cityEntity.uuid,
            name = cityEntity.name,
            timeZone = cityEntity.timeZone,
            isVisible = cityEntity.isVisible
        )
    }
}