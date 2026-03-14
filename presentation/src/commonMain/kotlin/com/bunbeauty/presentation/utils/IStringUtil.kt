package com.bunbeauty.presentation.utils

import androidx.compose.runtime.Composable
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.enums.ProductCode
import com.bunbeauty.domain.model.order.details.OrderAddress

interface IStringUtil {
    @Composable
    fun getDeferredTimeString(deferred: Long?): String?

    @Composable
    fun getCostString(cost: Int?): String

    @Composable
    fun getOrderCodeString(orderCode: String): String

    @Composable
    fun getReceiveMethodString(isDelivery: Boolean): String

    @Composable
    fun getProductCountString(count: Int): String

    @Composable
    fun getOrderStatusString(orderStatus: OrderStatus): String

    @Composable
    fun getOrderAddressString(address: OrderAddress): String

    @Composable
    fun getOrderStatusByString(orderStatus: String?): OrderStatus

    @Composable
    fun getDeliveryString(deliveryCost: Int): String

    @Composable
    fun getProductCodeString(productCode: ProductCode): String

    @Composable
    fun getProductCode(productCode: String): ProductCode?

    @Composable
    fun getBonusString(bonusCount: Int?): String
}
