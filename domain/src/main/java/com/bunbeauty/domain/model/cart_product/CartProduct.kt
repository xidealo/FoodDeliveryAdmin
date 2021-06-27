package com.bunbeauty.domain.model.cart_product

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.domain.model.ServerMenuProduct
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(
    var count: Int = 1,
    var menuProduct: MenuProduct,
) : Parcelable