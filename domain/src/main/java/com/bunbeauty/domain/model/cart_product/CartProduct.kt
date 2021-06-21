package com.bunbeauty.domain.model.cart_product

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bunbeauty.domain.model.MenuProduct
import kotlinx.parcelize.Parcelize

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