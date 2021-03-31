package com.bunbeauty.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class MenuProduct(
    @PrimaryKey
    override var uuid: String = "",
    var name: String = "",
    var cost: Int = 0,
    val discountCost: Int? = null,
    var weight: Int = 0,
    var description: String = "",
    val comboDescription: String = "",
    var photoLink: String = "",
    var onFire: Boolean = false,
    var inOven: Boolean = false,
    var productCode: String = "",
    var barcode: Int = 0,
    val visible: Boolean = true
) : BaseModel(), Parcelable