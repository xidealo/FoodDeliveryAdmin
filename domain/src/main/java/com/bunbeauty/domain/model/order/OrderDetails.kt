package com.bunbeauty.domain.model.order

import android.os.Parcelable
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.cart_product.CartProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderDetails(
    val uuid: String,
    val cafeUuid: String,
    val oderProductList: List<CartProduct>,
    val address: String?,
    val code: String,
    val comment: String?,
    val deferred: Long?,
    val delivery: Boolean,
    val discount: Int?,
    val email: String,
    val orderStatus: OrderStatus,
    val phone: String,
    val time: Long,
    val userId: String,
    val deliveryCost: Int? = null,
    val availableStatusList: List<OrderStatus>
) : Parcelable