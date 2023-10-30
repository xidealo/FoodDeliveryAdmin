package com.bunbeauty.presentation.feature.order.mapper

import android.annotation.SuppressLint
import android.content.res.Resources
import com.bunbeauty.common.Constants
import com.bunbeauty.common.Constants.PERCENT
import com.bunbeauty.common.Constants.RUBLE_CURRENCY
import com.bunbeauty.domain.model.order.details.OrderAddress
import com.bunbeauty.domain.model.order.details.OrderDetails
import com.bunbeauty.domain.util.datetime.DateTimeUtil
import com.bunbeauty.presentation.R
import com.bunbeauty.presentation.feature.order.state.OrderDetailsDataState
import com.bunbeauty.presentation.feature.order.state.OrderDetailsUiState
import javax.inject.Inject

class OrderDetailsStateMapper @Inject constructor(
    private val resources: Resources,
    private val dateTimeUtil: DateTimeUtil,
    private val orderStatusMapper: OrderStatusMapper,
    private val orderProductMapper: OrderProductMapper,
) {

    @SuppressLint("StringFormatInvalid")
    fun map(dataState: OrderDetailsDataState): OrderDetailsUiState {
        return OrderDetailsUiState(
            title = resources.getString(R.string.title_order_details, dataState.code),
            state = when (dataState.state) {
                OrderDetailsDataState.State.LOADING -> OrderDetailsUiState.State.Loading
                OrderDetailsDataState.State.ERROR -> OrderDetailsUiState.State.Error
                OrderDetailsDataState.State.SUCCESS -> {
                    if (dataState.orderDetails == null) {
                        OrderDetailsUiState.State.Error
                    } else {
                        OrderDetailsUiState.State.Success(
                            dateTime = dateTimeUtil.getDateTimeDDMMHHMM(dataState.orderDetails.time),
                            deferredTime = getDeferredTime(dataState.orderDetails),
                            paymentMethod = dataState.orderDetails.paymentMethod,
                            receiptMethod = getReceiptMethod(dataState.orderDetails),
                            address = getOrderAddressString(dataState.orderDetails.address),
                            comment = dataState.orderDetails.comment,
                            status = orderStatusMapper.map(dataState.orderDetails.status),
                            phoneNumber = dataState.orderDetails.clientUser.phoneNumber,
                            productList = dataState.orderDetails.oderProductList.map(
                                orderProductMapper::map
                            ),
                            deliveryCost = dataState.orderDetails.deliveryCost?.let { deliveryCost ->
                                "$deliveryCost $RUBLE_CURRENCY"
                            },
                            percentDiscount = dataState.orderDetails.percentDiscount?.let { percentDiscount ->
                                "$percentDiscount$PERCENT"
                            },
                            finalCost = "${dataState.orderDetails.newTotalCost} $RUBLE_CURRENCY",
                            oldFinalCost = dataState.orderDetails.oldTotalCost?.let { oldTotalCost ->
                                "${dataState.orderDetails.oldTotalCost} $RUBLE_CURRENCY"
                            }
                        )
                    }
                }
            },
            eventList = dataState.eventList,
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
                value = dateTimeUtil.getTimeHHMM(deferredTime)
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