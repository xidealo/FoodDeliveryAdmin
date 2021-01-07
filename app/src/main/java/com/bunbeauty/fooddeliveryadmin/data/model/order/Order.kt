package com.bunbeauty.fooddeliveryadmin.data.model.order

import android.content.Context
import android.os.Parcelable
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bunbeauty.fooddeliveryadmin.R
import com.bunbeauty.fooddeliveryadmin.data.model.BaseModel
import com.bunbeauty.fooddeliveryadmin.data.model.Time
import com.bunbeauty.fooddeliveryadmin.enums.OrderStatus
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTime

@Parcelize
@Entity
data class Order(
    var street: String = "",
    var house: String = "",
    var flat: String = "",
    var entrance: String = "",
    var intercom: String = "",
    var floor: String = "",
    var comment: String = "",
    var phone: String = "",
    var time: Long = 0,
    var orderStatus: OrderStatus = OrderStatus.NotAccepted,
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    override var uuid: String = "",
) : BaseModel(), Parcelable {
    fun getTimeHHMM(): String {
        val newTime = Time(time, 3)
        return newTime.toStringTimeHHMM()
    }

    fun getAddress() =
        "Доставка на улицу:$street\n" +
                "Дом:$house " +
                "Квартира:$flat " +
                "Подъезд:$entrance " +
                "Домофон:$intercom " +
                "Этаж:$floor\n" +
                "Комментарий:$comment\n" +
                "Контактный телефон:$phone"

    fun getUuidCode() = "Код заказа $uuid"

    fun getOrderStatusColor(order: Order): Int {
        return when (order.orderStatus) {
            OrderStatus.NotAccepted -> R.color.notAcceptedColor
            OrderStatus.Preparing -> R.color.preparingColor
            OrderStatus.SentOut -> R.color.sentOutColor
            OrderStatus.Delivered -> R.color.deliveredColor
        }
    }

    companion object {
        const val ORDERS = "ORDERS"

        const val STREET = "street"
        const val HOUSE = "house"
        const val FLAT = "flat"
        const val ENTRANCE = "entrance"
        const val INTERCOM = "intercom"
        const val FLOOR = "floor"
        const val COMMENT = "comment"
        const val PHONE = "phone"
        const val ORDER_STATUS = "order status"
        const val TIMESTAMP = "timestamp"
    }
}
