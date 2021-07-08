package com.bunbeauty.fooddeliveryadmin.utils

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.model.order.UserAddress

interface IStringUtil {
    fun getUserAddressString(userAddress: UserAddress?): String?
    fun getDeferredTimeString(deferred: String?): String
    fun getCostString(cost: Int?): String
    fun getOrderCodeString(orderCode: String): String
    fun getReceivingMethodString(isDelivery: Boolean): String
    fun getProductCountString(count: Int): String
    fun getOrderCountString(count: Int): String
    fun getOrderStatusString(orderStatus: OrderStatus): String
    fun getOrderStatusByString(orderStatus:String): OrderStatus
    fun getDeliveryString(deliveryCost: Int): String
    fun getProductCodeString(productCode: ProductCode): String
    fun getProductCode(productCode: String): ProductCode?
    fun getBonusString(bonusCount: Int?): String
}