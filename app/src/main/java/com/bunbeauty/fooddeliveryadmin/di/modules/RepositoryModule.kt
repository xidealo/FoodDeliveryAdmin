package com.bunbeauty.fooddeliveryadmin.di.modules

import com.bunbeauty.domain.repository.address.AddressRepo
import com.bunbeauty.domain.repository.address.AddressRepository
import com.bunbeauty.domain.repository.cafe.CafeRepo
import com.bunbeauty.domain.repository.cafe.CafeRepository
import com.bunbeauty.domain.repository.order.OrderRepo
import com.bunbeauty.domain.repository.order.OrderRepository
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