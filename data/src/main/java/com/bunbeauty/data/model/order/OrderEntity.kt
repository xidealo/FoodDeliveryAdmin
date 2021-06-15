package com.bunbeauty.data.model.order

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bunbeauty.data.enums.OrderStatus
import com.bunbeauty.data.model.Address
import com.bunbeauty.data.model.BaseDiffUtilModel
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTime
import java.util.*

@Parcelize
@Entity
data class OrderEntity(
    override var uuid: String = "",
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @Embedded(prefix = "address_")
    var address: Address = Address(),
    var comment: String = "",
    var phone: String = "",
    var time: Long = DateTime.now().millis,
    var orderStatus: OrderStatus = OrderStatus.NOT_ACCEPTED,
    var isDelivery: Boolean = false,
    var code: String = "",
    var email: String = "",
    var deferred: String? = null
) : BaseDiffUtilModel, Parcelable {

    companion object {
        const val ORDERS = "ORDERS"
        const val ORDER_ENTITY = "orderEntity"
        const val ORDER_STATUS = "orderStatus"
        const val TIMESTAMP = "timestamp"
    }
}
