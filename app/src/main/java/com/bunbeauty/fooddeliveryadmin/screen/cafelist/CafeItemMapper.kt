package com.bunbeauty.fooddeliveryadmin.screen.cafelist

import android.content.res.Resources
import com.bunbeauty.domain.model.cafe.CafeStatus
import com.bunbeauty.domain.model.cafe.CafeWithWorkingHours
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.screen.cafelist.item.CafeUiItem
import javax.inject.Inject

class CafeItemMapper @Inject constructor(private val resources: Resources) {

    fun map(cafe: CafeWithWorkingHours): CafeUiItem {
        return CafeUiItem(
            uuid = cafe.uuid,
            address = cafe.address,
            workingHours = cafe.workingHours,
            cafeStatusText = when (val status = cafe.status) {
                CafeStatus.Open -> resources.getString(R.string.msg_cafe_open)
                CafeStatus.Closed -> resources.getString(R.string.msg_cafe_closed)
                is CafeStatus.CloseSoon -> resources.getQuantityString(
                    R.plurals.msg_cafe_close_soon,
                    status.minutes,
                    status.minutes
                )
            },
            cafeStatus = cafe.status
        )
    }
}
