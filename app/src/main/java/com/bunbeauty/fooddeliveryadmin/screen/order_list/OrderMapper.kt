package com.bunbeauty.fooddeliveryadmin.screen.order_list

import android.content.res.Resources
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.util.date_time.DateTimeUtil
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.feature.order.mapper.OrderStatusMapper
import javax.inject.Inject

class OrderMapper @Inject constructor(
    private val orderStatusMapper: OrderStatusMapper,
    private val dateTimeUtil: DateTimeUtil,
    private val resources: Resources,
) {

    fun map(order: Order): OrderListUiState.OrderItem {
        return OrderListUiState.OrderItem(
            uuid = order.uuid,
            status = order.orderStatus,
            statusString = orderStatusMapper.map(order.orderStatus),
            code = order.code,
            deferredTime = getDeferredTime(order.deferredTime),
            dateTime = dateTimeUtil.getDateTimeDDMMHHMM(order.time)
        )
    }

    private fun getDeferredTime(deferredTime: Long?): String {
        return if (deferredTime == null) {
            ""
        } else {
            resources.getString(R.string.msg_order_deferred_date_time) +
                dateTimeUtil.getTimeHHMM(deferredTime)
        }
    }
}