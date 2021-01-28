package com.bunbeauty.fooddeliveryadmin.data.model.order

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.bunbeauty.fooddeliveryadmin.data.model.BaseModel
import com.bunbeauty.fooddeliveryadmin.data.model.CartProduct
import com.bunbeauty.fooddeliveryadmin.data.model.Time
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTime

@Parcelize
data class Order(
        @Embedded
    var orderEntity: OrderEntity = OrderEntity(),

        @Relation(parentColumn = "uuid", entityColumn = "orderUuid")
    var cartProducts: List<CartProduct> = ArrayList(),

    var timestamp: Long = DateTime.now().millis
) : BaseModel(), Parcelable {

    fun getTimeHHMM(): String {
        return Time(timestamp, 3).toStringTimeHHMM()
    }

    fun getFullPrice(): String {
        var fullPrice = 0
        for (cartProduct in cartProducts)
            fullPrice += cartProduct.count * cartProduct.menuProduct.cost

        return "Стоимость заказа: $fullPrice ₽"
    }
}