package com.bunbeauty.fooddeliveryadmin.data.model.order

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.bunbeauty.fooddeliveryadmin.data.model.Address
import com.bunbeauty.fooddeliveryadmin.data.model.BaseModel
import com.bunbeauty.fooddeliveryadmin.data.model.Time
import com.bunbeauty.fooddeliveryadmin.enums.OrderStatus
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTime

@Parcelize
@Entity
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    override var uuid: String = "",
    @Embedded(prefix = "address_")
    var address: Address = Address(),
    var comment: String = "",
    var phone: String = "",
    var time: Long = DateTime.now().millis,
    @Ignore
    var timestamp: Map<String, String>? = null,
    var orderStatus: OrderStatus = OrderStatus.NOT_ACCEPTED,
    var isDelivery: Boolean = true,
    var code: String = ""
) : BaseModel(), Parcelable {

    fun getTimeHHMM(): String {
        return Time(time, 3).toStringTimeHHMM()
    }

    companion object {
        const val ORDERS = "ORDERS"
        const val ORDER_ENTITY = "orderEntity"

        const val STREET = "street"
        const val HOUSE = "house"
        const val FLAT = "flat"
        const val ENTRANCE = "entrance"
        const val INTERCOM = "intercom"
        const val FLOOR = "floor"
        const val COMMENT = "comment"
        const val PHONE = "phone"
        const val ORDER_STATUS = "orderStatus"
        const val TIMESTAMP = "timestamp"
        const val CODE = "code"
    }
}
