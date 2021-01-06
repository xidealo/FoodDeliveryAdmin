package com.bunbeauty.fooddeliveryadmin.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bunbeauty.fooddeliveryadmin.data.local.converter.OrderStatusConverter
import com.bunbeauty.fooddeliveryadmin.data.local.db.order.OrderDao
import com.bunbeauty.fooddeliveryadmin.data.model.CartProduct
import com.bunbeauty.fooddeliveryadmin.data.model.MenuProduct
import com.bunbeauty.fooddeliveryadmin.data.model.order.Order
import com.bunbeauty.fooddeliveryadmin.data.local.converter.ProductCodeConverter
import com.bunbeauty.fooddeliveryadmin.enums.OrderStatus

@Database(
    entities = [
        CartProduct::class,
        MenuProduct::class,
        Order::class
    ], version = 2
)
@TypeConverters(ProductCodeConverter::class, OrderStatusConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
}