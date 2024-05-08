package com.bunbeauty.presentation.feature.order.mapper

import android.content.res.Resources
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.presentation.R
import javax.inject.Inject

class OrderStatusMapper @Inject constructor(
    private val resources: Resources
) {
    fun map(orderStatus: OrderStatus): String {
        return when (orderStatus) {
            OrderStatus.NOT_ACCEPTED -> resources.getString(R.string.msg_status_not_accepted)
            OrderStatus.ACCEPTED -> resources.getString(R.string.msg_status_accepted)
            OrderStatus.PREPARING -> resources.getString(R.string.msg_status_preparing)
            OrderStatus.SENT_OUT -> resources.getString(R.string.msg_status_sent_out)
            OrderStatus.DELIVERED -> resources.getString(R.string.msg_status_delivered)
            OrderStatus.DONE -> resources.getString(R.string.msg_status_ready)
            OrderStatus.CANCELED -> resources.getString(R.string.msg_status_canceled)
        }
    }
}
