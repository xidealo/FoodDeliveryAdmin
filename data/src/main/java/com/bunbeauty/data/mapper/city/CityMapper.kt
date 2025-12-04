package com.bunbeauty.data.mapper.city

import com.bunbeauty.data.model.server.city.CityServer
import com.bunbeauty.domain.model.city.City

class CityMapper {
    fun map(cityServer: CityServer): City =
        City(
            uuid = cityServer.uuid,
            name = cityServer.name,
            timeZone = cityServer.timeZone,
            isVisible = cityServer.isVisible,
        )
}
