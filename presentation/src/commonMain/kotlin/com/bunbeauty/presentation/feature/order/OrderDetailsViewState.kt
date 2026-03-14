package com.bunbeauty.presentation.feature.order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.cartproduct.OrderProduct
import com.bunbeauty.domain.model.order.details.OrderAddress
import com.bunbeauty.domain.model.order.details.OrderDetails
import com.bunbeauty.domain.model.order.details.PaymentMethod
import com.bunbeauty.domain.util.datetime.PATTERN_DD_MMMM_HH_MM
import com.bunbeauty.domain.util.datetime.PATTERN_HH_MM
import com.bunbeauty.presentation.feature.order.mapper.orderStatusMap
import com.bunbeauty.presentation.feature.order.state.OrderDetailsState
import com.bunbeauty.presentation.feature.orderlist.compose.getOrderColor
import com.bunbeauty.presentation.viewmodel.base.BaseViewState
import common.Constants
import common.Constants.BULLET_SYMBOL
import common.Constants.RUBLE_CURRENCY
import common.Constants.X_SYMBOL
import fooddeliveryadmin.presentation.generated.resources.Res
import fooddeliveryadmin.presentation.generated.resources.hint_order_details_delivery_deferred_time
import fooddeliveryadmin.presentation.generated.resources.hint_order_details_pickup_deferred_time
import fooddeliveryadmin.presentation.generated.resources.msg_address_entrance
import fooddeliveryadmin.presentation.generated.resources.msg_address_flat
import fooddeliveryadmin.presentation.generated.resources.msg_address_floor
import fooddeliveryadmin.presentation.generated.resources.msg_address_house
import fooddeliveryadmin.presentation.generated.resources.msg_payment_card
import fooddeliveryadmin.presentation.generated.resources.msg_payment_card_number
import fooddeliveryadmin.presentation.generated.resources.msg_payment_cash
import fooddeliveryadmin.presentation.generated.resources.msg_payment_phone_number
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.stringResource

@Immutable
data class OrderDetailsViewState(
    val title: String,
    val state: State,
) : BaseViewState {
    @Immutable
    sealed interface State {
        data object Loading : State

        data object Error : State

        data class Success(
            val dateTime: String,
            val deferredTime: HintWithValue?,
            val paymentMethod: String?,
            val receiptMethod: String,
            val address: String,
            val comment: String?,
            val status: String,
            val statusColor: Color,
            val phoneNumber: String,
            val productList: ImmutableList<Product>,
            val percentDiscount: String?,
            val deliveryCost: String?,
            val finalCost: String,
            val saving: Boolean,
            val statusListUI: StatusListUI,
        ) : State {
            @Immutable
            data class StatusListUI(
                val isShown: Boolean,
                val statusList: ImmutableList<StatusItem>,
            ) {
                @Immutable
                data class StatusItem(
                    val orderStatus: OrderStatus,
                    val status: String,
                )
            }
        }
    }

    @Immutable
    data class HintWithValue(
        val hint: String,
        val value: String,
    )

    @Immutable
    data class Product(
        val title: String,
        val price: String,
        val count: String,
        val cost: String,
        val description: String?,
    )
}

@Composable
internal fun OrderDetailsState.DataState.toViewState(): OrderDetailsViewState =
    OrderDetailsViewState(
        title = "Заказ «$code»",
        state =
            when (state) {
                OrderDetailsState.DataState.State.LOADING -> OrderDetailsViewState.State.Loading
                OrderDetailsState.DataState.State.ERROR -> OrderDetailsViewState.State.Error
                OrderDetailsState.DataState.State.SUCCESS -> {
                    orderDetails?.let { details ->
                        OrderDetailsViewState.State.Success(
                            dateTime =
                                DateTimeUtil.formatDateTime(
                                    orderDetails.time,
                                    PATTERN_DD_MMMM_HH_MM,
                                ),
                            deferredTime = getDeferredTime(details),
                            paymentMethod = details.paymentMethod?.map(),
                            receiptMethod = getReceiptMethod(orderDetails),
                            address = getOrderAddressString(details.address),
                            comment = details.comment,
                            status = orderStatusMap(details.status),
                            phoneNumber = details.clientUser.phoneNumber,
                            productList =
                                orderDetails.oderProductList
                                    .map(
                                        ::map,
                                    ).toPersistentList(),
                            deliveryCost = details.deliveryCost?.let { "$it ₽" },
                            percentDiscount = details.percentDiscount?.let { "$it%" },
                            finalCost = "${details.newTotalCost} ₽",
                            saving = saving,
                            statusListUI =
                                OrderDetailsViewState.State.Success.StatusListUI(
                                    isShown = showStatusList,
                                    statusList =
                                        details.availableStatusList
                                            .map { status ->
                                                OrderDetailsViewState.State.Success.StatusListUI.StatusItem(
                                                    orderStatus = status,
                                                    status = orderStatusMap(status),
                                                )
                                            }.toPersistentList(),
                                ),
                            statusColor =
                                getOrderColor(
                                    orderStatus = orderDetails.status,
                                ),
                        )
                    } ?: OrderDetailsViewState.State.Error
                }
            },
    )

@Composable
private fun getDeferredTime(orderDetails: OrderDetails): OrderDetailsViewState.HintWithValue? =
    orderDetails.deferredTime?.let { deferredTime ->
        val hintStringId =
            if (orderDetails.isDelivery) {
                Res.string.hint_order_details_delivery_deferred_time
            } else {
                Res.string.hint_order_details_pickup_deferred_time
            }
        OrderDetailsViewState.HintWithValue(
            hint = stringResource(hintStringId),
            value = DateTimeUtil.formatDateTime(deferredTime, PATTERN_HH_MM),
        )
    }

private fun getReceiptMethod(details: OrderDetails): String = if (details.isDelivery) "Доставка" else "Самовывоз"

@Composable
private fun getOrderAddressString(address: OrderAddress): String =
    address.description ?: (
        address.street +
            getAddressPart(
                part =
                    Constants.ADDRESS_DIVIDER +
                        stringResource(
                            Res.string.msg_address_house,
                            address.house.orEmpty(),
                        ),
                data = address.house,
            ) +
            getAddressPart(
                part =
                    Constants.ADDRESS_DIVIDER +
                        stringResource(
                            Res.string.msg_address_flat,
                            address.flat.orEmpty(),
                        ),
                data = address.flat,
            ) +
            getAddressPart(
                part =
                    Constants.ADDRESS_DIVIDER +
                        stringResource(
                            Res.string.msg_address_entrance,
                            address.entrance.orEmpty(),
                        ),
                data = address.entrance,
            ) +
            getAddressPart(
                part =
                    Constants.ADDRESS_DIVIDER +
                        stringResource(
                            Res.string.msg_address_floor,
                            address.floor.orEmpty(),
                        ),
                data = address.floor,
            ) +
            getAddressPart(
                part = Constants.ADDRESS_DIVIDER + address.comment.orEmpty(),
                data = address.comment,
            )
    )

private fun getAddressPart(
    part: String,
    data: String?,
): String =
    if (data.isNullOrEmpty()) {
        ""
    } else {
        part
    }

fun map(orderProduct: OrderProduct): OrderDetailsViewState.Product =
    OrderDetailsViewState.Product(
        title = orderProduct.name,
        price =
            if (orderProduct.additionsPrice == null) {
                "${orderProduct.newPrice} $RUBLE_CURRENCY"
            } else {
                "(${orderProduct.newPrice} $RUBLE_CURRENCY + ${orderProduct.additionsPrice} $RUBLE_CURRENCY)"
            },
        count = "$X_SYMBOL ${orderProduct.count}",
        cost = "${orderProduct.newTotalCost} $RUBLE_CURRENCY",
        description = getDescription(orderProduct = orderProduct),
    )

private fun getDescription(orderProduct: OrderProduct): String? {
    val additions = getAdditionsString(orderProduct = orderProduct)
    return if (orderProduct.comboDescription.isNullOrEmpty() && additions == null) {
        null
    } else {
        buildString {
            if (orderProduct.comboDescription != null) {
                append(orderProduct.comboDescription)
                if (additions != null) {
                    append("\n")
                }
            }
            if (additions != null) {
                append(additions)
            }
        }
    }
}

private fun getAdditionsString(orderProduct: OrderProduct): String? =
    orderProduct.orderAdditions
        .takeIf { additions ->
            additions.isNotEmpty()
        }?.let { additions ->
            additions.joinToString(" $BULLET_SYMBOL ") { orderAddition ->
                orderAddition.name
            }
        }

@Composable
fun PaymentMethod.map(): String =
    when (this) {
        PaymentMethod.CASH -> Res.string.msg_payment_cash
        PaymentMethod.CARD -> Res.string.msg_payment_card
        PaymentMethod.CARD_NUMBER -> Res.string.msg_payment_card_number
        PaymentMethod.PHONE_NUMBER -> Res.string.msg_payment_phone_number
    }.let { nameResId ->
        stringResource(nameResId)
    }
