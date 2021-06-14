package com.bunbeauty.fooddeliveryadmin.utils

import com.bunbeauty.data.model.Address

interface IStringUtil {
    fun toString(address: Address?): String
    fun getDeferredTimeString(deferred: String): String
    fun getCostString(cost: Int?): String
    fun getOrderCodeString(orderCode: String): String
    fun getOrderReceivingMethod(isDelivery: Boolean): String
}