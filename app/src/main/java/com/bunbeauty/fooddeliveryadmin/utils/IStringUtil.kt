package com.bunbeauty.fooddeliveryadmin.utils

import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.model.Address

interface IStringUtil {
    fun toString(address: Address?): String
    fun getDeferredTimeString(deferred: String?): String
    fun getCostString(cost: Int?): String
    fun getProceedsString(proceeds: Int): String
    fun getOrderCodeString(orderCode: String): String
    fun getReceivingMethodString(isDelivery: Boolean): String
    fun getProductCountString(count: Int): String
    fun getOrderCountString(count: Int): String
    fun getOrderStatusString(orderStatus: OrderStatus): String
    fun getOrderStatusByString(orderStatus:String): OrderStatus
}