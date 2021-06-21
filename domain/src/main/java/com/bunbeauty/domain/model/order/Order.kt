package com.bunbeauty.domain.model.order

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.bunbeauty.domain.model.cart_product.CartProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    var uuid: String = "",

    @Embedded
    var orderEntity: OrderEntity = OrderEntity(),

    @Relation(parentColumn = "id", entityColumn = "orderId")
    var cartProducts: List<CartProduct> = ArrayList(),

    @Ignore
    var cafeId: String = "",

    @Ignore
    var timestamp: Long = 0
) : Parcelable