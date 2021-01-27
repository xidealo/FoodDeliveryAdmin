package com.bunbeauty.fooddeliveryadmin.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class CartProduct(
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    override var uuid: String = "",
    @Embedded(prefix = "menuProduct") var menuProduct: MenuProduct = MenuProduct(),
    var count: Int = 1,
    var discount: Float = 0f,
    var orderUuid: String = ""
) : BaseModel(), Parcelable {

    companion object {
        const val CART_PRODUCT = "cart product"
        const val CART_PRODUCTS = "cartProducts"

        const val COUNT = "count"
        const val DISCOUNT = "discount"
    }
}