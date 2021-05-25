package com.bunbeauty.domain

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bunbeauty.data.OrderStatusConverter
import com.bunbeauty.data.model.Address
import com.bunbeauty.data.model.CafeEntity
import com.bunbeauty.data.model.cart_product.CartProduct
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.data.model.order.OrderEntity
import com.bunbeauty.domain.repository.address.AddressDao
import com.bunbeauty.domain.repository.cafe.CafeDao
import com.bunbeauty.domain.repository.menu_product.MenuProductDao
import com.bunbeauty.domain.repository.order.OrderDao

@Database(
        entities = [
            CartProduct::class,
            MenuProduct::class,
            OrderEntity::class,
            Address::class,
            CafeEntity::class,
        ], version = 14
)
@TypeConverters(OrderStatusConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun addressDao(): AddressDao
    abstract fun cafeDao(): CafeDao
    abstract fun menuProductDao(): MenuProductDao
}