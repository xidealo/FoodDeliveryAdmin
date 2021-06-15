package com.bunbeauty.data.model.order

import android.os.Parcelable
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.model.cart_product.CartProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderUI(
    val status: OrderStatus,
    val code: String,
    val deferredTime: String,
    val time: String,
    val isDelivery: Boolean,
    val comment: String,
    val email: String,
    val phone: String,
    val address: String,
    val cartProductList: List<CartProduct>,
    val newTotalCost: String,
    val oldTotalCost: String,
): Parcelable