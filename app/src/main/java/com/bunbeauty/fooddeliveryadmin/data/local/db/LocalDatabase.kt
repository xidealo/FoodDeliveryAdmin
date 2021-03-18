package com.bunbeauty.fooddeliveryadmin.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bunbeauty.fooddeliveryadmin.data.local.converter.OrderStatusConverter
import com.bunbeauty.fooddeliveryadmin.data.local.converter.ProductCodeConverter
import com.bunbeauty.fooddeliveryadmin.data.local.db.address.AddressDao
import com.bunbeauty.fooddeliveryadmin.data.local.db.cafe.CafeDao
import com.bunbeauty.fooddeliveryadmin.data.local.db.order.OrderDao
import com.bunbeauty.data.model.Address
import com.bunbeauty.data.model.CafeEntity
import com.bunbeauty.data.model.CartProduct
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.data.model.order.OrderEntity

@Database(
        entities = [
            CartProduct::class,
            MenuProduct::class,
            OrderEntity::class,
            Address::class,
            CafeEntity::class,
        ], version = 8
)
@TypeConverters(ProductCodeConverter::class, OrderStatusConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun addressDao(): AddressDao
    abstract fun cafeDao(): CafeDao
}