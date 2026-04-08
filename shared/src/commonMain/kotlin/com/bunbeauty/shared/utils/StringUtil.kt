package com.bunbeauty.shared.utils

import DateTimeUtil
import androidx.compose.runtime.Composable
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.enums.OrderStatus.ACCEPTED
import com.bunbeauty.domain.enums.OrderStatus.CANCELED
import com.bunbeauty.domain.enums.OrderStatus.DELIVERED
import com.bunbeauty.domain.enums.OrderStatus.DONE
import com.bunbeauty.domain.enums.OrderStatus.NOT_ACCEPTED
import com.bunbeauty.domain.enums.OrderStatus.PREPARING
import com.bunbeauty.domain.enums.OrderStatus.SENT_OUT
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.enums.ProductCode.BAKERY
import com.bunbeauty.domain.enums.ProductCode.BARBECUE
import com.bunbeauty.domain.enums.ProductCode.BURGER
import com.bunbeauty.domain.enums.ProductCode.COMBO
import com.bunbeauty.domain.enums.ProductCode.DRINK
import com.bunbeauty.domain.enums.ProductCode.OVEN
import com.bunbeauty.domain.enums.ProductCode.PIZZA
import com.bunbeauty.domain.enums.ProductCode.POTATO
import com.bunbeauty.domain.enums.ProductCode.SPICE
import com.bunbeauty.domain.model.order.details.OrderAddress
import com.bunbeauty.domain.util.datetime.PATTERN_HH_MM
import common.Constants.ADDRESS_DIVIDER
import fooddeliveryadmin.shared.generated.resources.Res
import fooddeliveryadmin.shared.generated.resources.msg_address_entrance
import fooddeliveryadmin.shared.generated.resources.msg_address_flat
import fooddeliveryadmin.shared.generated.resources.msg_address_floor
import fooddeliveryadmin.shared.generated.resources.msg_address_house
import fooddeliveryadmin.shared.generated.resources.msg_order
import fooddeliveryadmin.shared.generated.resources.msg_order_deferred_date_time
import fooddeliveryadmin.shared.generated.resources.msg_order_delivery
import fooddeliveryadmin.shared.generated.resources.msg_order_delivery_free
import fooddeliveryadmin.shared.generated.resources.msg_order_pickup
import fooddeliveryadmin.shared.generated.resources.msg_pieces
import fooddeliveryadmin.shared.generated.resources.msg_product_code_bakery
import fooddeliveryadmin.shared.generated.resources.msg_product_code_barbecue
import fooddeliveryadmin.shared.generated.resources.msg_product_code_burger
import fooddeliveryadmin.shared.generated.resources.msg_product_code_combo
import fooddeliveryadmin.shared.generated.resources.msg_product_code_drink
import fooddeliveryadmin.shared.generated.resources.msg_product_code_oven
import fooddeliveryadmin.shared.generated.resources.msg_product_code_pizza
import fooddeliveryadmin.shared.generated.resources.msg_product_code_potato
import fooddeliveryadmin.shared.generated.resources.msg_product_code_spice
import fooddeliveryadmin.shared.generated.resources.msg_ruble
import fooddeliveryadmin.shared.generated.resources.msg_status_accepted
import fooddeliveryadmin.shared.generated.resources.msg_status_canceled
import fooddeliveryadmin.shared.generated.resources.msg_status_delivered
import fooddeliveryadmin.shared.generated.resources.msg_status_not_accepted
import fooddeliveryadmin.shared.generated.resources.msg_status_preparing
import fooddeliveryadmin.shared.generated.resources.msg_status_ready
import fooddeliveryadmin.shared.generated.resources.msg_status_sent_out
import org.jetbrains.compose.resources.stringResource

class StringUtil(
    private val dateTimeUtil: DateTimeUtil,
) : IStringUtil {
    @Composable
    override fun getDeferredTimeString(deferred: Long?): String =
        if (deferred == null) {
            ""
        } else {
            stringResource(Res.string.msg_order_deferred_date_time) +
                dateTimeUtil.formatDateTime(deferred, PATTERN_HH_MM)
        }

    @Composable
    override fun getCostString(cost: Int?): String =
        if (cost == null) {
            ""
        } else {
            cost.toString() + stringResource(Res.string.msg_ruble)
        }

    @Composable
    override fun getOrderCodeString(orderCode: String): String = stringResource(Res.string.msg_order) + orderCode

    @Composable
    override fun getReceiveMethodString(isDelivery: Boolean): String =
        if (isDelivery) {
            stringResource(Res.string.msg_order_delivery)
        } else {
            stringResource(Res.string.msg_order_pickup)
        }

    @Composable
    override fun getProductCountString(count: Int): String = stringResource(Res.string.msg_pieces) + count

    @Composable
    override fun getOrderStatusString(orderStatus: OrderStatus): String =
        when (orderStatus) {
            NOT_ACCEPTED -> stringResource(Res.string.msg_status_not_accepted)
            ACCEPTED -> stringResource(Res.string.msg_status_accepted)
            PREPARING -> stringResource(Res.string.msg_status_preparing)
            SENT_OUT -> stringResource(Res.string.msg_status_sent_out)
            DELIVERED -> stringResource(Res.string.msg_status_delivered)
            DONE -> stringResource(Res.string.msg_status_ready)
            CANCELED -> stringResource(Res.string.msg_status_canceled)
        }

    @Composable
    override fun getOrderStatusByString(orderStatus: String?): OrderStatus =
        when (orderStatus) {
            stringResource(Res.string.msg_status_not_accepted) -> NOT_ACCEPTED
            stringResource(Res.string.msg_status_accepted) -> ACCEPTED
            stringResource(Res.string.msg_status_preparing) -> PREPARING
            stringResource(Res.string.msg_status_sent_out) -> SENT_OUT
            stringResource(Res.string.msg_status_delivered) -> DELIVERED
            stringResource(Res.string.msg_status_ready) -> DONE
            stringResource(Res.string.msg_status_canceled) -> CANCELED
            else -> NOT_ACCEPTED
        }

    @Composable
    override fun getOrderAddressString(address: OrderAddress): String =
        address.description ?: (
            address.street +
                getAddressPart(
                    part =
                        ADDRESS_DIVIDER +
                            stringResource(
                                Res.string.msg_address_house,
                                address.house.orEmpty(),
                            ),
                    data = address.house,
                ) +
                address.flat?.let { flat ->
                    getAddressPart(
                        part =
                            ADDRESS_DIVIDER +
                                stringResource(
                                    Res.string.msg_address_flat,
                                    flat,
                                ),
                        data = address.flat,
                    )
                } +
                address.entrance?.let { entrance ->
                    getAddressPart(
                        part =
                            ADDRESS_DIVIDER +
                                stringResource(
                                    Res.string.msg_address_entrance,
                                    entrance,
                                ),
                        data = address.entrance,
                    )
                } +
                address.floor?.let { floor ->
                    getAddressPart(
                        part =
                            ADDRESS_DIVIDER +
                                stringResource(
                                    Res.string.msg_address_floor,
                                    floor,
                                ),
                        data = address.floor,
                    )
                } +
                getAddressPart(
                    part = ADDRESS_DIVIDER + address.comment,
                    data = address.comment,
                )
        )

    @Composable
    override fun getDeliveryString(deliveryCost: Int): String =
        if (deliveryCost == 0) {
            stringResource(Res.string.msg_order_delivery_free)
        } else {
            getCostString(deliveryCost)
        }

    @Composable
    override fun getProductCodeString(productCode: ProductCode): String =
        when (productCode) {
            COMBO -> stringResource(Res.string.msg_product_code_combo)
            PIZZA -> stringResource(Res.string.msg_product_code_pizza)
            BARBECUE -> stringResource(Res.string.msg_product_code_barbecue)
            BURGER -> stringResource(Res.string.msg_product_code_burger)
            DRINK -> stringResource(Res.string.msg_product_code_drink)
            POTATO -> stringResource(Res.string.msg_product_code_potato)
            SPICE -> stringResource(Res.string.msg_product_code_spice)
            BAKERY -> stringResource(Res.string.msg_product_code_bakery)
            OVEN -> stringResource(Res.string.msg_product_code_oven)
        }

    @Composable
    override fun getProductCode(productCode: String): ProductCode? =
        when (productCode) {
            stringResource(Res.string.msg_product_code_combo) -> COMBO
            stringResource(Res.string.msg_product_code_pizza) -> PIZZA
            stringResource(Res.string.msg_product_code_barbecue) -> BARBECUE
            stringResource(Res.string.msg_product_code_burger) -> BURGER
            stringResource(Res.string.msg_product_code_drink) -> DRINK
            stringResource(Res.string.msg_product_code_potato) -> POTATO
            stringResource(Res.string.msg_product_code_spice) -> SPICE
            stringResource(Res.string.msg_product_code_bakery) -> BAKERY
            stringResource(Res.string.msg_product_code_oven) -> OVEN
            else -> null
        }

    @Composable
    override fun getBonusString(bonusCount: Int?): String =
        if (bonusCount == null) {
            ""
        } else {
            "-" + getCostString(bonusCount)
        }

    fun getAddressPart(
        part: String,
        data: String?,
    ): String =
        if (data.isNullOrEmpty()) {
            ""
        } else {
            part
        }
}
