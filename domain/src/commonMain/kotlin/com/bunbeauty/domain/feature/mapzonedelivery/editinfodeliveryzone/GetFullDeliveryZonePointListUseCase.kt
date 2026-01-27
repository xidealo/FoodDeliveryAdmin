package com.bunbeauty.domain.feature.mapzonedelivery.editinfodeliveryzone

import com.bunbeauty.domain.model.cafe.DeliveryZone
import com.bunbeauty.domain.model.cafe.DeliveryZonePoint

class GetFullDeliveryZonePointListUseCase {
    operator fun invoke(deliveryZone: DeliveryZone): List<DeliveryZonePoint> {
        val positions = deliveryZone.deliveryZonePoint
        return if (positions.isNotEmpty() && positions.first() != positions.last()) {
            positions.plusElement(positions.first())
        } else {
            positions
        }
    }
}
