package com.bunbeauty.domain.model.order

import android.os.Parcelable
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.cart_product.CartProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    val uuid: String,
    val code: String,
    val time: Long,
    val deferredTime: Long?,
    val timeZone: String,
    val orderStatus: OrderStatus
) : Parcelable