package com.bunbeauty.fooddeliveryadmin.screen.orderlist

import android.content.res.Resources
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.util.datetime.DateTimeUtil
import com.bunbeauty.domain.util.datetime.PATTERN_DD_MMMM_HH_MM
import com.bunbeauty.domain.util.datetime.PATTERN_HH_MM
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.feature.order.mapper.OrderStatusMapper
import javax.inject.Inject

class OrderMapper @Inject constructor(
    private val orderStatusMapper: OrderStatusMapper,
    private val dateTimeUtil: DateTimeUtil,
    private val resources: Resources
) {

    fun map(order: Order): OrderListViewState.OrderItem {
        return OrderListViewState.OrderItem(
            uuid = order.uuid,
            status = order.orderStatus,
            statusString = orderStatusMapper.map(order.orderStatus),
            code = order.code,
            deferredTime = getDeferredTime(order.deferredTime),
            dateTime = dateTimeUtil.formatDateTime(order.time, PATTERN_DD_MMMM_HH_MM)
        )
    }

    private fun getDeferredTime(deferredTime: Long?): String {
        return if (deferredTime == null) {
            ""
        } else {
            resources.getString(R.string.msg_order_deferred_date_time) +
                dateTimeUtil.formatDateTime(deferredTime, PATTERN_HH_MM)
        }
    }
}
