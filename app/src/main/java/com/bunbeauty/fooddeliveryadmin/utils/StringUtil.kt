package com.bunbeauty.fooddeliveryadmin.utils

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.enums.OrderStatus.*
import com.bunbeauty.domain.model.order.UserAddress
import com.bunbeauty.domain.util.resources.ResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import javax.inject.Inject

class StringUtil @Inject constructor(private val resourcesProvider: ResourcesProvider) :
    IStringUtil {

    override fun getUserAddressString(userAddress: UserAddress?): String? {
        return if (userAddress == null) {
            null
        } else {
            userAddress.street +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_house),
                        userAddress.house
                    ) +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_flat),
                        userAddress.flat
                    ) +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_entrance),
                        userAddress.entrance
                    ) +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_intercom),
                        userAddress.intercom
                    ) +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_floor),
                        userAddress.floor
                    )
        }
    }

    override fun getDeferredTimeString(deferred: String?): String {
        return if (deferred.isNullOrEmpty()) {
            ""
        } else {
            resourcesProvider.getString(R.string.msg_order_item_deferred_time) + deferred
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
        return resourcesProvider.getString(R.string.msg_order_details_order) + orderCode
    }

    override fun getReceivingMethodString(isDelivery: Boolean): String {
        return if (isDelivery) {
            resourcesProvider.getString(R.string.msg_order_details_delivery)
        } else {
            resourcesProvider.getString(R.string.msg_order_details_pickup)
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
            resourcesProvider.getString(R.string.msg_order_details_delivery_free)
        } else {
            getCostString(deliveryCost)
        }
    }
}