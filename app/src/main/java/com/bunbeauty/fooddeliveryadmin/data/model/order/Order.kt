package com.bunbeauty.fooddeliveryadmin.data.model.order

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.bunbeauty.fooddeliveryadmin.data.model.BaseModel
import com.bunbeauty.fooddeliveryadmin.data.model.CartProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    @Embedded
    var orderEntity: OrderEntity = OrderEntity(),

    @Relation(parentColumn = "uuid", entityColumn = "orderUuid")
    var cartProducts: List<CartProduct> = ArrayList()
) : BaseModel(), Parcelable {
    fun getFullPrice(): String {
        var fullPrice = 0
        for (cartProduct in cartProducts)
            fullPrice += cartProduct.count * cartProduct.menuProduct.cost

        return "Стоимость заказа: $fullPrice ₽"
    }
}