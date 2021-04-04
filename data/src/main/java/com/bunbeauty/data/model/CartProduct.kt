package com.bunbeauty.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class CartProduct(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @Embedded(prefix = "menuProduct")
    var menuProduct: MenuProduct = MenuProduct(),
    var count: Int = 1,
    var discount: Float = 0f,
    var orderId: Long? = null
) : Parcelable