package com.bunbeauty.fooddeliveryadmin.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bunbeauty.fooddeliveryadmin.enums.ProductCode
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class MenuProduct(
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    override var uuid: String = "",
    var name: String = "",
    var cost: Int = 0,
    var weight: Int = 0,
    var description: String = "",
    var photoLink: String = "",
    var onFire: Boolean = false,
    var inOven: Boolean = false,
    var productCode: ProductCode = ProductCode.ALL,
    var barcode: Int = 0
) : BaseModel(), Parcelable {

    fun getStringCost() = "$cost ₽"
    fun getStringGram() = "$weight г."

    companion object {
        const val PRODUCTS: String = "products"
        const val MENU_PRODUCT: String = "menuProduct"

        const val NAME: String = "name"
        const val COST: String = "cost"
        const val DESCRIPTION: String = "description"
        const val PHOTO_LINK: String = "photo link"
        const val PRODUCT_CODE: String = "product code"
    }
}