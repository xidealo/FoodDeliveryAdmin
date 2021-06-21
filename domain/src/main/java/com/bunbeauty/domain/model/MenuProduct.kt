package com.bunbeauty.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class MenuProduct(
    @PrimaryKey
    var uuid: String = "",
    var name: String = "",
    var cost: Int = 0,
    var discountCost: Int? = null,
    var weight: Int = 0,
    var description: String = "",
    var comboDescription: String = "",
    var photoLink: String = "",
    var onFire: Boolean = false,
    var inOven: Boolean = false,
    var productCode: String = "",
    var barcode: Int = 0,
    var visible: Boolean = true
) : Parcelable

fun List<MenuProduct>.getNewMenuUuid(): String {
    val menuProduct = this.maxByOrNull { it.uuid.replace("-", "").toInt() }!!
    return "-${ menuProduct.uuid.replace("-", "").toInt() + 1}"
}