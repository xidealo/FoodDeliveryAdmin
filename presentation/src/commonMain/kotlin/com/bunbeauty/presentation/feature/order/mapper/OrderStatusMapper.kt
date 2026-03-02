package com.bunbeauty.presentation.feature.order.mapper

import androidx.compose.runtime.Composable
import com.bunbeauty.domain.enums.OrderStatus
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.msg_status_accepted
import fooddeliveryadmin.presentation.generated.resources.msg_status_canceled
import fooddeliveryadmin.presentation.generated.resources.msg_status_delivered
import fooddeliveryadmin.presentation.generated.resources.msg_status_not_accepted
import fooddeliveryadmin.presentation.generated.resources.msg_status_preparing
import fooddeliveryadmin.presentation.generated.resources.msg_status_ready
import fooddeliveryadmin.presentation.generated.resources.msg_status_sent_out
import org.jetbrains.compose.resources.stringResource


@Composable
fun orderStatusMap(orderStatus: OrderStatus): String =
    when (orderStatus) {
        OrderStatus.NOT_ACCEPTED -> stringResource(Res.string.msg_status_not_accepted)
        OrderStatus.ACCEPTED -> stringResource(Res.string.msg_status_accepted)
        OrderStatus.PREPARING -> stringResource(Res.string.msg_status_preparing)
        OrderStatus.SENT_OUT -> stringResource(Res.string.msg_status_sent_out)
        OrderStatus.DELIVERED -> stringResource(Res.string.msg_status_delivered)
        OrderStatus.DONE -> stringResource(Res.string.msg_status_ready)
        OrderStatus.CANCELED -> stringResource(Res.string.msg_status_canceled)
    }
