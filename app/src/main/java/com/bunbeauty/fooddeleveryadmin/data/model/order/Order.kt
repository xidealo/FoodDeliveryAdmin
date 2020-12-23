package com.bunbeauty.fooddeleveryadmin.data.model.order

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bunbeauty.fooddeleveryadmin.data.model.BaseModel
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
    var time: Long = DateTime.now().millis,
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    override var uuid: String = "",
) : BaseModel(), Parcelable {

    fun getAddress() = "Доставка на улицу $street дом $house контактный телефон $phone"
    fun getUuidCode() = "Код заказа $uuid"

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
    }
}
