package com.bunbeauty.data

import androidx.room.TypeConverter
import com.bunbeauty.data.enums.OrderStatus

class OrderStatusConverter {
    @TypeConverter
    fun convertFromOrderStatus(orderStatus: OrderStatus): String {
        return orderStatus.name
    }

    @TypeConverter
    fun convertToOrderStatus(name: String): OrderStatus {
        return OrderStatus.valueOf(name)
    }
}