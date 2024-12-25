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
import com.bunbeauty.data.repository.SettingsRepository
import com.bunbeauty.data.repository.StatisticRepository
import com.bunbeauty.data.repository.UserAuthorizationRepository
import com.bunbeauty.domain.feature.order.OrderRepo
import com.bunbeauty.domain.repo.AdditionGroupRepo
import com.bunbeauty.domain.repo.AdditionRepo
import com.bunbeauty.domain.repo.CafeRepo
import com.bunbeauty.domain.repo.CategoryRepo
import com.bunbeauty.domain.repo.CityRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.repo.MenuProductRepo
import com.bunbeauty.domain.repo.NonWorkingDayRepo
import com.bunbeauty.domain.repo.PhotoRepo
import com.bunbeauty.domain.repo.SettingsRepo
import com.bunbeauty.domain.repo.StatisticRepo
import com.bunbeauty.domain.repo.UserAuthorizationRepo
import org.koin.dsl.module

fun repositoryModule() = module {
    single<MenuProductRepo> {
        MenuProductRepository(
            menuProductMapper = get(),
            networkConnector = get()
        )
    }
    single<FoodDeliveryApi> {
        FoodDeliveryApiImpl(
            client = get(),
            json = get()
        )
    }
    single<DataStoreRepo> {
        DataStoreRepository(
            context = get()
        )
    }
    single<UserAuthorizationRepo> {
        UserAuthorizationRepository(
            context = get(),
            dataStoreRepo = get(),
            networkConnector = get()
        )
    }
    single<CategoryRepo> {
        CategoryRepository(
            categoryMapper = get(),
            networkConnector = get()
        )
    }
    single<OrderRepo> {
        OrderRepository(
            networkConnector = get(),
            serverOrderMapper = get()
        )
    }
    single<StatisticRepo> {
        StatisticRepository(
            networkConnector = get(),
            statisticMapper = get()
        )
    }
    single<CafeRepo> {
        CafeRepository(
            cafeMapper = get(),
            cafeDao = get(),
            foodDeliveryApi = get()
        )
    }
        single<CityRepo> {
        CityRepository(
            cityDao = get(),
            cityMapper = get(),
            foodDeliveryApi = get()
        )
    }
    single<NonWorkingDayRepo> {
        NonWorkingDayRepository(
            foodDeliveryApi = get(),
            nonWorkingDayDao = get(),
            nonWorkingDayMapper = get()
        )
    }
    single<AdditionRepo> {
        AdditionRepository(
            networkConnector = get()
        )
    }
    single<AdditionGroupRepo> {
        AdditionGroupRepository(
            networkConnector = get()
        )
    }
    single<PhotoRepo> {
        PhotoRepository(
            context = get()
        )
    }
    single<SettingsRepo> {
        SettingsRepository(
            dataStoreRepo = get(),
            foodDeliveryApi = get()
        )
    }
}
