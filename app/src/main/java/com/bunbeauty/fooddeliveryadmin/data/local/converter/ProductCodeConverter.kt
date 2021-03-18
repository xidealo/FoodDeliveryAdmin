package com.bunbeauty.fooddeliveryadmin.data.local.converter

import androidx.room.TypeConverter
import com.bunbeauty.data.enums.ProductCode

class ProductCodeConverter {
    @TypeConverter
    fun convertFromProductCode(productCode: ProductCode): String {
        return productCode.name
    }

    @TypeConverter
    fun convertToProductCode(name: String): ProductCode {
        return ProductCode.valueOf(name)
    }
}