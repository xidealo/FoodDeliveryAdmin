package com.bunbeauty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bunbeauty.domain.model.address.Address
import com.bunbeauty.domain.model.cafe.CafeEntity
import com.bunbeauty.domain.model.cart_product.CartProduct
import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.domain.model.order.OrderEntity
import com.bunbeauty.data.dao.AddressDao
import com.bunbeauty.data.dao.CafeDao
import com.bunbeauty.data.dao.MenuProductDao
import com.bunbeauty.data.dao.OrderDao

@Database(
        entities = [
            CartProduct::class,
            MenuProduct::class,
            OrderEntity::class,
            Address::class,
            CafeEntity::class,
        ], version = 16
)
@TypeConverters(OrderStatusConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun addressDao(): AddressDao
    abstract fun cafeDao(): CafeDao
    abstract fun menuProductDao(): MenuProductDao
}