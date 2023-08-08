package com.bunbeauty.presentation.utils

import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.model.order.details.OrderAddress

interface IStringUtil {
    fun getDeferredTimeString(deferred: Long?): String?
    fun getCostString(cost: Int?): String
    fun getOrderCodeString(orderCode: String): String
    fun getReceiveMethodString(isDelivery: Boolean): String
    fun getProductCountString(count: Int): String
    fun getOrderCountString(count: Int): String
    fun getOrderStatusString(orderStatus: OrderStatus): String
    fun getOrderAddressString(address: OrderAddress): String
    fun getOrderStatusByString(orderStatus:String?): OrderStatus
    fun getDeliveryString(deliveryCost: Int): String
    fun getProductCodeString(productCode: ProductCode): String
    fun getProductCode(productCode: String): ProductCode?
    fun getBonusString(bonusCount: Int?): String
}