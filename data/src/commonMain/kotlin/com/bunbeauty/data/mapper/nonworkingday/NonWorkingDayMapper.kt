package com.bunbeauty.data.mapper.nonworkingday

import com.bunbeauty.data.model.server.nonworkingday.NonWorkingDayServer
import com.bunbeauty.data.model.server.nonworkingday.PostNonWorkingDayServer
import com.bunbeauty.domain.model.nonworkingday.NewNonWorkingDay
import com.bunbeauty.domain.model.nonworkingday.NonWorkingDay

class NonWorkingDayMapper {
    fun toNonWorkingDay(nonWorkingDayServer: NonWorkingDayServer): NonWorkingDay =
        nonWorkingDayServer.run {
            NonWorkingDay(
                uuid = uuid,
                timestamp = timestamp,
                cafeUuid = cafeUuid,
                isVisible = isVisible,
            )
        }

    fun toPostNonWorkingDayServer(newNonWorkingDay: NewNonWorkingDay): PostNonWorkingDayServer =
        newNonWorkingDay.run {
            PostNonWorkingDayServer(
                timestamp = timestamp,
                cafeUuid = cafeUuid,
            )
        }
}
