package com.bunbeauty.domain.model.order

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bunbeauty.domain.enums.OrderStatus
import com.bunbeauty.domain.model.address.Address
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTime

@Parcelize
@Entity
data class OrderEntity(
    var uuid: String = "",
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
) : Parcelable
