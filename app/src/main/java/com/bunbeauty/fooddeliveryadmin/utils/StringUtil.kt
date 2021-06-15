package com.bunbeauty.fooddeliveryadmin.utils

import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.enums.OrderStatus.*
import com.bunbeauty.data.model.Address
import com.bunbeauty.domain.resources.ResourcesProvider
import com.bunbeauty.fooddeliveryadmin.R
import javax.inject.Inject

class StringUtil @Inject constructor(private val resourcesProvider: ResourcesProvider) :
    IStringUtil {

    override fun toString(address: Address?): String {
        return if (address == null) {
            ""
        } else {
            address.street.name +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_house),
                        address.house
                    ) +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_flat),
                        address.flat
                    ) +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_entrance),
                        address.entrance
                    ) +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_intercom),
                        address.intercom
                    ) +
                    getStringPart(
                        resourcesProvider.getString(R.string.msg_address_floor),
                        address.floor
                    )
        }
    }

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
        return resourcesProvider.getString(R.string.msg_order_details_order) + orderCode
    }

    override fun getReceivingMethodString(isDelivery: Boolean): String {
        return if (isDelivery) {
            resourcesProvider.getString(R.string.msg_order_delivery)
        } else {
            resourcesProvider.getString(R.string.msg_order_pickup)
        }
    }

    override fun getCountString(count: Int): String {
        return resourcesProvider.getString(R.string.msg_pieces) + count
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

    fun getStringPart(constant: String, possiblyEmpty: String): String {
        return if (possiblyEmpty.isEmpty()) {
            ""
        } else {
            constant + possiblyEmpty
        }
    }
}