package com.bunbeauty.data.model.order

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.bunbeauty.data.model.BaseModel
import com.bunbeauty.data.model.CartProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    @Embedded
    var orderEntity: OrderEntity = OrderEntity(),

    @Relation(parentColumn = "id", entityColumn = "orderId")
    var cartProducts: List<CartProduct> = ArrayList(),

    @Ignore
    var cafeId: String = "",

    @Ignore
    var timestamp: Long = 0

) : BaseModel(), Parcelable {
    fun getFullPrice(): String {
        var fullPrice = 0
        for (cartProduct in cartProducts)
            fullPrice += cartProduct.count * cartProduct.menuProduct.cost

        return "Стоимость заказа: $fullPrice ₽"
    }
}