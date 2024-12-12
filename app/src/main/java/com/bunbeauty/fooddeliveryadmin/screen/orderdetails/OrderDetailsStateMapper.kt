package com.bunbeauty.fooddeliveryadmin.screen.orderdetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
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
import com.bunbeauty.presentation.feature.order.state.OrderDetailsState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import javax.inject.Inject

class OrderDetailsStateMapper @Inject constructor(
    private val dateTimeUtil: DateTimeUtil,
    private val orderStatusMapper: OrderStatusMapper,
    private val orderProductMapper: OrderProductMapper,
    private val paymentMethodMapper: PaymentMethodMapper
) {

    @Composable
    fun map(dataState: OrderDetailsState.DataState): OrderDetailsViewState {
        return OrderDetailsViewState(
            title = stringResource(R.string.title_order_details, dataState.code),
            state = when (dataState.state) {
                OrderDetailsState.DataState.State.LOADING -> OrderDetailsViewState.State.Loading
                OrderDetailsState.DataState.State.ERROR -> OrderDetailsViewState.State.Error
                OrderDetailsState.DataState.State.SUCCESS -> {
                    dataState.orderDetails?.let { orderDetails ->
                        OrderDetailsViewState.State.Success(
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
                            ).toPersistentList(),
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
                            saving = dataState.saving,
                            statusListUI = OrderDetailsViewState.State.Success.StatusListUI(
                                isShown = dataState.showStatusList,
                                statusList = dataState.orderDetails?.availableStatusList
                                    ?.map { orderStatus ->
                                        OrderDetailsViewState.State.Success.StatusListUI.StatusItem(
                                            orderStatus = orderStatus,
                                            status = orderStatusMapper.map(orderStatus)
                                        )
                                    }?.toPersistentList() ?: persistentListOf()
                            )
                        )
                    } ?: OrderDetailsViewState.State.Error
                }
            }
        )
    }

    @Composable
    private fun getDeferredTime(orderDetails: OrderDetails): OrderDetailsViewState.HintWithValue? {
        return orderDetails.deferredTime?.let { deferredTime ->
            val hintStringId = if (orderDetails.isDelivery) {
                R.string.hint_order_details_delivery_deferred_time
            } else {
                R.string.hint_order_details_pickup_deferred_time
            }
            OrderDetailsViewState.HintWithValue(
                hint = stringResource(hintStringId),
                value = dateTimeUtil.formatDateTime(deferredTime, PATTERN_HH_MM)
            )
        }
    }

    @Composable
    private fun getReceiptMethod(orderDetails: OrderDetails): String {
        return if (orderDetails.isDelivery) {
            R.string.msg_order_details_delivery
        } else {
            R.string.msg_order_details_pickup
        }.let { stringId ->
            stringResource(stringId)
        }
    }

    @Composable
    private fun getOrderAddressString(address: OrderAddress): String {
        return address.description ?: (
            address.street +
                getAddressPart(
                    part = Constants.ADDRESS_DIVIDER + stringResource(
                        R.string.msg_address_house,
                        address.house.orEmpty()
                    ),
                    data = address.house
                ) +
                getAddressPart(
                    part = Constants.ADDRESS_DIVIDER + stringResource(
                        R.string.msg_address_flat,
                        address.flat.orEmpty()
                    ),
                    data = address.flat
                ) +
                getAddressPart(
                    part = Constants.ADDRESS_DIVIDER + stringResource(
                        R.string.msg_address_entrance,
                        address.entrance.orEmpty()
                    ),
                    data = address.entrance
                ) +
                getAddressPart(
                    part = Constants.ADDRESS_DIVIDER + stringResource(
                        R.string.msg_address_floor,
                        address.floor.orEmpty()
                    ),
                    data = address.floor
                ) +
                getAddressPart(
                    part = Constants.ADDRESS_DIVIDER + address.comment.orEmpty(),
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
