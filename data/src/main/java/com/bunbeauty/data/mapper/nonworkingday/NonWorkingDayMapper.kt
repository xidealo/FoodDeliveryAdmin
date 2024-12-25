package com.bunbeauty.data.mapper.nonworkingday

import com.bunbeauty.data.model.entity.NonWorkingDayEntity
import com.bunbeauty.data.model.server.nonworkingday.NonWorkingDayServer
import com.bunbeauty.data.model.server.nonworkingday.PostNonWorkingDayServer
import com.bunbeauty.domain.model.nonworkingday.NewNonWorkingDay
import com.bunbeauty.domain.model.nonworkingday.NonWorkingDay


class NonWorkingDayMapper {

    fun toNonWorkingDay(nonWorkingDayServer: NonWorkingDayServer): NonWorkingDay {
        return nonWorkingDayServer.run {
            NonWorkingDay(
                uuid = uuid,
                timestamp = timestamp,
                cafeUuid = cafeUuid,
                isVisible = isVisible
            )
        }
    }

    fun toNonWorkingDay(nonWorkingDayEntity: NonWorkingDayEntity): NonWorkingDay {
        return nonWorkingDayEntity.run {
            NonWorkingDay(
                uuid = uuid,
                timestamp = timestamp,
                cafeUuid = cafeUuid,
                isVisible = isVisible
            )
        }
    }

    fun toNonWorkingDayEntity(nonWorkingDay: NonWorkingDay): NonWorkingDayEntity {
        return nonWorkingDay.run {
            NonWorkingDayEntity(
                uuid = uuid,
                timestamp = timestamp,
                cafeUuid = cafeUuid,
                isVisible = isVisible
            )
        }
    }

    fun toPostNonWorkingDayServer(newNonWorkingDay: NewNonWorkingDay): PostNonWorkingDayServer {
        return newNonWorkingDay.run {
            PostNonWorkingDayServer(
                timestamp = timestamp,
                cafeUuid = cafeUuid
            )
        }
    }
}
