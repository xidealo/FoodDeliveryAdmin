package com.bunbeauty.data.di

import com.bunbeauty.data.NetworkConnector
import com.bunbeauty.data.repository.*
import com.bunbeauty.domain.repo.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Singleton
    @Binds
    abstract fun bindOrderRepo(orderRepository: OrderRepository): OrderRepo

    @Singleton
    @Binds
    abstract fun bindCafeRepo(cafeRepository: CafeRepository): CafeRepo

    @Singleton
    @Binds
    abstract fun bindMenuProductRepo(menuProductRepository: MenuProductRepository): MenuProductRepo

    @Singleton
    @Binds
    abstract fun bindDeliveryRepo(deliveryRepository: DeliveryRepository): DeliveryRepo

    @Singleton
    @Binds
    abstract fun bindApiRepository(networkConnectorImpl: NetworkConnectorImpl): NetworkConnector

    @Singleton
    @Binds
    abstract fun bindDataStoreRepository(dataStoreRepository: DataStoreRepository): DataStoreRepo

    @Singleton
    @Binds
    abstract fun bindUserAuthorizationRepository(userAuthorizationRepository: UserAuthorizationRepository): UserAuthorizationRepo
}