package com.bunbeauty.presentation.feature.orderlist.state

import DateTimeUtil
import androidx.compose.runtime.Composable
import com.bunbeauty.domain.model.order.Order
import com.bunbeauty.domain.util.datetime.PATTERN_DD_MMMM_HH_MM
import com.bunbeauty.domain.util.datetime.PATTERN_HH_MM
import com.bunbeauty.presentation.feature.order.mapper.OrderStatusMapper
import fooddeliveryadmin.presentation.generated.resources.Res
import org.jetbrains.compose.resources.stringResource

class OrderMapper(
    private val orderStatusMapper: OrderStatusMapper,
    private val dateTimeUtil: DateTimeUtil,

) {
    @Composable
    fun map(order: Order): OrderListViewState.OrderItem =
        OrderListViewState.OrderItem(
            uuid = order.uuid,
            status = order.orderStatus,
            statusString = orderStatusMapper.map(order.orderStatus),
            code = order.code,
            deferredTime = getDeferredTime(order.deferredTime),
            dateTime = dateTimeUtil.formatDateTime(order.time, PATTERN_DD_MMMM_HH_MM),
        )

    @Composable
    private fun getDeferredTime(deferredTime: Long?): String =
        if (deferredTime == null) {
            ""
        } else {
            stringResource(Res.string.msg_order_deferred_date_time) +
                dateTimeUtil.formatDateTime(deferredTime, PATTERN_HH_MM)
        }
}
