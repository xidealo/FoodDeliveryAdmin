package com.bunbeauty.fooddeliveryadmin.di.modules

import com.bunbeauty.fooddeliveryadmin.data.local.db.address.AddressRepo
import com.bunbeauty.fooddeliveryadmin.data.local.db.address.AddressRepository
import com.bunbeauty.fooddeliveryadmin.data.local.db.cafe.CafeRepo
import com.bunbeauty.fooddeliveryadmin.data.local.db.cafe.CafeRepository
import com.bunbeauty.fooddeliveryadmin.data.local.db.order.OrderRepo
import com.bunbeauty.fooddeliveryadmin.data.local.db.order.OrderRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun bindOrderRepo(orderRepository: OrderRepository): OrderRepo

    @Binds
    abstract fun bindAddressRepo(addressRepository: AddressRepository): AddressRepo

    @Binds
    abstract fun bindCafeRepo(cafeRepository: CafeRepository): CafeRepo
}