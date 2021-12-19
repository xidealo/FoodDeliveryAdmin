package com.bunbeauty.presentation.utils

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.enums.OrderStatus.*
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.enums.ProductCode.*
import com.bunbeauty.domain.model.order.UserAddress
import com.bunbeauty.presentation.R
import javax.inject.Inject

class StringUtil @Inject constructor(private val resourcesProvider: ResourcesProvider) :
    IStringUtil {

    override fun getDeferredTimeString(deferred: String?): String {
        return if (deferred.isNullOrEmpty()) {
            ""
        } else {
            resourcesProvider.getString(R.string.msg_order_deferred_time) + deferred
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

    override fun getReceivingMethodString(isDelivery: Boolean): String {
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

    override fun getOrderStatusByString(orderStatus: String): OrderStatus {
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

    fun getStringPart(constant: String, possiblyEmpty: String?): String {
        return if (possiblyEmpty.isNullOrEmpty()) {
            ""
        } else {
            constant + possiblyEmpty
        }
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
}