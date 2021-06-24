package com.bunbeauty.fooddeliveryadmin.di.modules

import com.bunbeauty.data.repository.*
import com.bunbeauty.domain.repo.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindOrderRepo(orderRepository: OrderRepository): OrderRepo

    @Singleton
    @Binds
    abstract fun bindAddressRepo(addressRepository: AddressRepository): AddressRepo

    @Singleton
    @Binds
    abstract fun bindCafeRepo(cafeRepository: CafeRepository): CafeRepo

    @Singleton
    @Binds
    abstract fun bindMenuProductRepo(menuProductRepository: MenuProductRepository): MenuProductRepo

    @Singleton
    @Binds
    abstract fun bindDeliveryRepo(deliveryRepository: DeliveryRepository): DeliveryRepo

    //NETWORK

    @Singleton
    @Binds
    abstract fun bindApiRepository(apiRepository: ApiRepository): ApiRepo

    // DATA_STORE

    @Singleton
    @Binds
    abstract fun bindDataStoreRepository(dataStoreRepository: DataStoreRepository): DataStoreRepo
}