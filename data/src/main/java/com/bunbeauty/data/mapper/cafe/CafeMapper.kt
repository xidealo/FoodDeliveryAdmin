package com.bunbeauty.data.mapper.cafe

import com.bunbeauty.data.model.server.cafe.CafeServer
import com.bunbeauty.data.model.server.cafe.PatchCafeServer
import com.bunbeauty.domain.model.cafe.Cafe
import com.bunbeauty.domain.model.cafe.UpdateCafe
import com.bunbeauty.domain.model.settings.WorkLoad
import com.bunbeauty.domain.model.settings.WorkType

class CafeMapper {
    fun toCafe(cafeServer: CafeServer): Cafe =
        cafeServer.run {
            Cafe(
                uuid = uuid,
                address = address,
                latitude = latitude,
                longitude = longitude,
                fromTime = fromTime,
                toTime = toTime,
                offset = offset,
                phone = phone,
                visible = isVisible,
                cityUuid = cityUuid,
                workload = WorkLoad.valueOf(workload),
                workType = WorkType.valueOf(workType),
                additional = additionalUtensils,
            )
        }

    fun toPatchCafeServer(cafe: Cafe): PatchCafeServer =
        cafe.run {
            PatchCafeServer(
                address = address,
                latitude = latitude,
                longitude = longitude,
                fromTime = fromTime,
                toTime = toTime,
                phone = phone,
                isVisible = visible,
                workload = workload.name,
                workType = workType.name,
                additionalUtensils = additional,
            )
        }

    fun toPatchCafe(updateCafe: UpdateCafe): PatchCafeServer =
        with(updateCafe) {
            PatchCafeServer(
                fromTime = fromTime,
                toTime = toTime,
                phone = phone,
                latitude = latitude,
                longitude = longitude,
                address = address,
                isVisible = visible,
                workload = workload?.name,
                workType = workType?.name,
                additionalUtensils = additionalUtensils,
            )
        }
}
