package com.bunbeauty.presentation.utils

import android.content.res.Resources
import com.bunbeauty.common.Constants.ADDRESS_DIVIDER
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
import com.bunbeauty.domain.util.datetime.DateTimeUtil
import com.bunbeauty.presentation.R
import javax.inject.Inject

class StringUtil @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
    private val resources: Resources,
    private val dateTimeUtil: DateTimeUtil
) : IStringUtil {

    override fun getDeferredTimeString(deferred: Long?): String {
        return if (deferred == null) {
            ""
        } else {
            resourcesProvider.getString(R.string.msg_order_deferred_date_time) + dateTimeUtil.getTimeHHMM(
                deferred
            )
        }
    }

    override fun getCostString(cost: Int?): String {
        return if (cost == null) {
            ""
        } else {
            cost.toString() + resourcesProvider.getString(R.string.msg_ruble)
        }
    }

    override fun getOrderCodeString(orderCode: String): String {
        return resourcesProvider.getString(R.string.msg_order) + orderCode
    }

    override fun getReceiveMethodString(isDelivery: Boolean): String {
        return if (isDelivery) {
            resourcesProvider.getString(R.string.msg_order_delivery)
        } else {
            resourcesProvider.getString(R.string.msg_order_pickup)
        }
    }

    override fun getProductCountString(count: Int): String {
        return resourcesProvider.getString(R.string.msg_pieces) + count
    }

    override fun getOrderCountString(count: Int): String {
        return resourcesProvider.getString(R.string.msg_statistic_orders) + count
    }

    override fun getOrderStatusString(orderStatus: OrderStatus): String {
        return when (orderStatus) {
            NOT_ACCEPTED -> resourcesProvider.getString(R.string.msg_status_not_accepted)
            ACCEPTED -> resourcesProvider.getString(R.string.msg_status_accepted)
            PREPARING -> resourcesProvider.getString(R.string.msg_status_preparing)
            SENT_OUT -> resourcesProvider.getString(R.string.msg_status_sent_out)
            DELIVERED -> resourcesProvider.getString(R.string.msg_status_delivered)
            DONE -> resourcesProvider.getString(R.string.msg_status_ready)
            CANCELED -> resourcesProvider.getString(R.string.msg_status_canceled)
        }
    }

    override fun getOrderStatusByString(orderStatus: String?): OrderStatus {
        return when (orderStatus) {
            resourcesProvider.getString(R.string.msg_status_not_accepted) -> NOT_ACCEPTED
            resourcesProvider.getString(R.string.msg_status_accepted) -> ACCEPTED
            resourcesProvider.getString(R.string.msg_status_preparing) -> PREPARING
            resourcesProvider.getString(R.string.msg_status_sent_out) -> SENT_OUT
            resourcesProvider.getString(R.string.msg_status_delivered) -> DELIVERED
            resourcesProvider.getString(R.string.msg_status_ready) -> DONE
            resourcesProvider.getString(R.string.msg_status_canceled) -> CANCELED
            else -> NOT_ACCEPTED
        }
    }

    override fun getOrderAddressString(address: OrderAddress): String {
        return address.description ?: (
            address.street +
                getAddressPart(
                    part = ADDRESS_DIVIDER + resources.getString(
                        R.string.msg_address_house,
                        address.house
                    ),
                    data = address.house
                ) +
                getAddressPart(
                    part = ADDRESS_DIVIDER + resources.getString(
                        R.string.msg_address_flat,
                        address.flat
                    ),
                    data = address.flat
                ) +
                getAddressPart(
                    part = ADDRESS_DIVIDER + resources.getString(
                        R.string.msg_address_entrance,
                        address.entrance
                    ),
                    data = address.entrance
                ) +
                getAddressPart(
                    part = ADDRESS_DIVIDER + resources.getString(
                        R.string.msg_address_floor,
                        address.floor
                    ),
                    data = address.floor
                ) +
                getAddressPart(
                    part = ADDRESS_DIVIDER + address.comment,
                    data = address.comment
                )
            )
    }

    override fun getDeliveryString(deliveryCost: Int): String {
        return if (deliveryCost == 0) {
            resourcesProvider.getString(R.string.msg_order_delivery_free)
        } else {
            getCostString(deliveryCost)
        }
    }

    override fun getProductCodeString(productCode: ProductCode): String {
        return when (productCode) {
            COMBO -> resourcesProvider.getString(R.string.msg_product_code_combo)
            PIZZA -> resourcesProvider.getString(R.string.msg_product_code_pizza)
            BARBECUE -> resourcesProvider.getString(R.string.msg_product_code_barbecue)
            BURGER -> resourcesProvider.getString(R.string.msg_product_code_burger)
            DRINK -> resourcesProvider.getString(R.string.msg_product_code_drink)
            POTATO -> resourcesProvider.getString(R.string.msg_product_code_potato)
            SPICE -> resourcesProvider.getString(R.string.msg_product_code_spice)
            BAKERY -> resourcesProvider.getString(R.string.msg_product_code_bakery)
            OVEN -> resourcesProvider.getString(R.string.msg_product_code_oven)
            else -> ""
        }
    }

    override fun getProductCode(productCode: String): ProductCode? {
        return when (productCode) {
            resourcesProvider.getString(R.string.msg_product_code_combo) -> COMBO
            resourcesProvider.getString(R.string.msg_product_code_pizza) -> PIZZA
            resourcesProvider.getString(R.string.msg_product_code_barbecue) -> BARBECUE
            resourcesProvider.getString(R.string.msg_product_code_burger) -> BURGER
            resourcesProvider.getString(R.string.msg_product_code_drink) -> DRINK
            resourcesProvider.getString(R.string.msg_product_code_potato) -> POTATO
            resourcesProvider.getString(R.string.msg_product_code_spice) -> SPICE
            resourcesProvider.getString(R.string.msg_product_code_bakery) -> BAKERY
            resourcesProvider.getString(R.string.msg_product_code_oven) -> OVEN
            else -> null
        }
    }

    override fun getBonusString(bonusCount: Int?): String {
        return if (bonusCount == null) {
            ""
        } else {
            "-" + getCostString(bonusCount)
        }
    }

    fun getAddressPart(part: String, data: String?): String {
        return if (data.isNullOrEmpty()) {
            ""
        } else {
            part
        }
    }

}