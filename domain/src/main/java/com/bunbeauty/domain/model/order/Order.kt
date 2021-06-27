package com.bunbeauty.domain.model.order

import android.os.Parcelable
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.cart_product.CartProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val uuid: String,
    val cafeUuid: String,
    val cartProductList: List<CartProduct>,
    val address: UserAddress?,
    val code: String,
    val comment: String?,
    val deferred: String?,
    val delivery: Boolean,
    val email: String,
    val orderStatus: OrderStatus,
    val phone: String,
    val time: Long,
    val userId: String,
) : Parcelable