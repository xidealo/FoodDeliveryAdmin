package com.bunbeauty.fooddeliveryadmin.di.modules

import com.bunbeauty.domain.repo.AddressRepo
import com.bunbeauty.data.repository.AddressRepository
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.data.repository.CafeRepository
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.data.repository.MenuProductRepository
import com.bunbeauty.domain.repo.OrderRepo
import com.bunbeauty.data.repository.OrderRepository
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

    @Binds
    abstract fun bindMenuProductRepoRepo(menuProductRepository: MenuProductRepository): MenuProductRepo
}