package com.bunbeauty.data.di

import com.bunbeauty.data.FoodDeliveryApi
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
    abstract fun bindMenuProductRepo(menuProductRepository: MenuProductRepository): MenuProductRepo

    @Singleton
    @Binds
    abstract fun bindApiRepository(networkConnectorImpl: FoodDeliveryApiImpl): FoodDeliveryApi

    @Singleton
    @Binds
    abstract fun bindDataStoreRepository(dataStoreRepository: DataStoreRepository): DataStoreRepo

    @Singleton
    @Binds
    abstract fun bindUserAuthorizationRepository(userAuthorizationRepository: UserAuthorizationRepository): UserAuthorizationRepo

    @Singleton
    @Binds
    abstract fun bindCategoryRepository(categoryRepository: CategoryRepository): CategoryRepo

    @Singleton
    @Binds
    abstract fun bindOrderRepository(orderRepository: OrderRepository): OrderRepo

    @Singleton
    @Binds
    abstract fun bindStatisticRepository(statisticRepository: StatisticRepository): StatisticRepo

    @Singleton
    @Binds
    abstract fun bindCafeRepository(cafeRepository: CafeRepository): CafeRepo

    @Singleton
    @Binds
    abstract fun bindCityRepository(cityRepository: CityRepository): CityRepo

    @Singleton
    @Binds
    abstract fun bindNonWorkingDayRepository(nonWorkingDayRepository: NonWorkingDayRepository): NonWorkingDayRepo

    @Singleton
    @Binds
    abstract fun bindAdditionRepository(additionRepository: AdditionRepository): AdditionRepo

}