package com.bunbeauty.fooddeliveryadmin.screen.orderdetails

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.runtime.Composable
import com.bunbeauty.common.Constants
import com.bunbeauty.common.Constants.PERCENT
import com.bunbeauty.common.Constants.RUBLE_CURRENCY
import com.bunbeauty.domain.model.order.details.OrderAddress
import com.bunbeauty.domain.model.order.details.OrderDetails
import com.bunbeauty.domain.util.datetime.DateTimeUtil
import com.bunbeauty.domain.util.datetime.PATTERN_DD_MMMM_HH_MM
import com.bunbeauty.domain.util.datetime.PATTERN_HH_MM
import com.bunbeauty.fooddeliveryadmin.screen.orderlist.compose.getOrderColor
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.feature.order.mapper.OrderStatusMapper
import com.bunbeauty.presentation.feature.order.state.OrderDetailsDataState
import javax.inject.Inject

class OrderDetailsStateMapper @Inject constructor(
    private val resources: Resources,
    private val dateTimeUtil: DateTimeUtil,
    private val orderStatusMapper: OrderStatusMapper,
    private val orderProductMapper: OrderProductMapper,
    private val paymentMethodMapper: PaymentMethodMapper
) {

    @Composable
    @SuppressLint("StringFormatInvalid")
    fun map(dataState: OrderDetailsDataState): OrderDetailsUiState {
        return OrderDetailsUiState(
            title = resources.getString(R.string.title_order_details, dataState.code),
            state = when (dataState.state) {
                OrderDetailsDataState.State.LOADING -> OrderDetailsUiState.State.Loading
                OrderDetailsDataState.State.ERROR -> OrderDetailsUiState.State.Error
                OrderDetailsDataState.State.SUCCESS -> {
                    dataState.orderDetails?.let { orderDetails ->
                        OrderDetailsUiState.State.Success(
                            dateTime = dateTimeUtil.formatDateTime(
                                orderDetails.time,
                                PATTERN_DD_MMMM_HH_MM
                            ),
                            deferredTime = getDeferredTime(orderDetails),
                            paymentMethod = orderDetails.paymentMethod?.let { paymentMethod ->
                                paymentMethodMapper.map(paymentMethod)
                            },
                            receiptMethod = getReceiptMethod(orderDetails),
                            address = getOrderAddressString(orderDetails.address),
                            comment = orderDetails.comment,
                            status = orderStatusMapper.map(orderDetails.status),
                            phoneNumber = orderDetails.clientUser.phoneNumber,
                            productList = orderDetails.oderProductList.map(
                                orderProductMapper::map
                            ),
                            deliveryCost = orderDetails.deliveryCost?.let { deliveryCost ->
                                "$deliveryCost $RUBLE_CURRENCY"
                            },
                            percentDiscount = orderDetails.percentDiscount?.let { percentDiscount ->
                                "$percentDiscount$PERCENT"
                            },
                            finalCost = "${orderDetails.newTotalCost} $RUBLE_CURRENCY",
                            statusColor = getOrderColor(
                                orderStatus = orderDetails.status
                            ),
                            saving = dataState.saving
                        )
                    } ?: OrderDetailsUiState.State.Error
                }
            },
            eventList = dataState.eventList
        )
    }

    private fun getDeferredTime(orderDetails: OrderDetails): OrderDetailsUiState.HintWithValue? {
        return orderDetails.deferredTime?.let { deferredTime ->
            val hintStringId = if (orderDetails.isDelivery) {
                R.string.hint_order_details_delivery_deferred_time
            } else {
                R.string.hint_order_details_pickup_deferred_time
            }
            OrderDetailsUiState.HintWithValue(
                hint = resources.getString(hintStringId),
                value = dateTimeUtil.formatDateTime(deferredTime, PATTERN_HH_MM)
            )
        }
    }

    private fun getReceiptMethod(orderDetails: OrderDetails): String {
        return if (orderDetails.isDelivery) {
            R.string.msg_order_details_delivery
        } else {
            R.string.msg_order_details_pickup
        }.let { stringId ->
            resources.getString(stringId)
        }
    }

    private fun getOrderAddressString(address: OrderAddress): String {
        return address.description ?: (
            address.street +
                getAddressPart(
                    part = Constants.ADDRESS_DIVIDER + resources.getString(
                        R.string.msg_address_house,
                        address.house
                    ),
                    data = address.house
                ) +
                getAddressPart(
                    part = Constants.ADDRESS_DIVIDER + resources.getString(
                        R.string.msg_address_flat,
                        address.flat
                    ),
                    data = address.flat
                ) +
                getAddressPart(
                    part = Constants.ADDRESS_DIVIDER + resources.getString(
                        R.string.msg_address_entrance,
                        address.entrance
                    ),
                    data = address.entrance
                ) +
                getAddressPart(
                    part = Constants.ADDRESS_DIVIDER + resources.getString(
                        R.string.msg_address_floor,
                        address.floor
                    ),
                    data = address.floor
                ) +
                getAddressPart(
                    part = Constants.ADDRESS_DIVIDER + address.comment,
                    data = address.comment
                )
            )
    }

    private fun getAddressPart(part: String, data: String?): String {
        return if (data.isNullOrEmpty()) {
            ""
        } else {
            part
        }
    }
}
