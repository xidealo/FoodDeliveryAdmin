package com.bunbeauty.fooddeliveryadmin.data.local.converter

import androidx.room.TypeConverter
import com.bunbeauty.fooddeliveryadmin.enums.OrderStatus

class OrderStatusConverter {
    @TypeConverter
    fun convertFromOrderStatus(orderStatus: OrderStatus): String {
        return orderStatus.name
    }

    @TypeConverter
    fun convertToProductCode(name: String): OrderStatus {
        return OrderStatus.valueOf(name)
    }
}