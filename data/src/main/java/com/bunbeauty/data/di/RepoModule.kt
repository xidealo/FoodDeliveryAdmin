package com.bunbeauty.data.di

import com.bunbeauty.data.FoodDeliveryApi
import com.bunbeauty.data.repository.AdditionGroupRepository
import com.bunbeauty.data.repository.AdditionRepository
import com.bunbeauty.data.repository.CafeRepository
import com.bunbeauty.data.repository.CategoryRepository
import com.bunbeauty.data.repository.CityRepository
import com.bunbeauty.data.repository.DataStoreRepository
import com.bunbeauty.data.repository.FoodDeliveryApiImpl
import com.bunbeauty.data.repository.MenuProductRepository
import com.bunbeauty.data.repository.NonWorkingDayRepository
import com.bunbeauty.data.repository.OrderRepository
import com.bunbeauty.data.repository.PhotoRepository
import com.bunbeauty.data.repository.StatisticRepository
import com.bunbeauty.data.repository.UserAuthorizationRepository
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.CityRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.repo.NonWorkingDayRepo
import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.repo.PhotoRepo
import com.bunbeauty.domain.repo.StatisticRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
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

    @Singleton
    @Binds
    abstract fun bindAdditionGroupRepository(additionGroupRepository: AdditionGroupRepository): AdditionGroupRepo

    @Singleton
    @Binds
    abstract fun bindPhotoRepository(photoRepository: PhotoRepository): PhotoRepo
}
