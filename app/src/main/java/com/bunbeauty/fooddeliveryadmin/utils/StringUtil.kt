package com.bunbeauty.fooddeliveryadmin.utils

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

    override fun getDeferredTimeString(deferred: String): String {
        return if (deferred.isNotEmpty()) {
            resourcesProvider.getString(R.string.msg_order_deferred_time) + deferred
        } else {
            ""
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

    override fun getOrderReceivingMethod(isDelivery: Boolean): String {
        return if (isDelivery) {
            resourcesProvider.getString(R.string.msg_order_delivery)
        } else {
            resourcesProvider.getString(R.string.msg_order_pickup)
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