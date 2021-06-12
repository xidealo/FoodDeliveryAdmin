package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.data.repository.ApiRepository
import com.bunbeauty.domain.util.cost.CostUtil
import com.bunbeauty.domain.util.cost.ICostUtil
import com.bunbeauty.domain.util.date_time.DateTimeUtil
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.domain.util.order.IOrderUtil
import com.bunbeauty.domain.util.order.OrderUtil
import com.bunbeauty.domain.util.product.IProductUtil
import com.bunbeauty.domain.util.product.ProductUtil
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.domain.util.resources.ResourcesProvider
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import com.bunbeauty.fooddeliveryadmin.utils.StringUtil
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApiModule {

    // UTIL

    @Singleton
    @Binds
    abstract fun bindStringUtil(stringUtil: StringUtil): IStringUtil

    @Singleton
    @Binds
    abstract fun bindCostUtil(costUtil: CostUtil): ICostUtil

    @Singleton
    @Binds
    abstract fun bindProductUtil(productUtil: ProductUtil): IProductUtil

    @Singleton
    @Binds
    abstract fun bindDateTimeUtil(dateTimeUtil: DateTimeUtil): IDateTimeUtil

    @Singleton
    @Binds
    abstract fun bindOrderUtil(orderUtil: OrderUtil): IOrderUtil

    // RESOURCE PROVIDER

    @Singleton
    @Binds
    abstract fun bindResourcesProvider(resourcesProvider: ResourcesProvider): IResourcesProvider

}