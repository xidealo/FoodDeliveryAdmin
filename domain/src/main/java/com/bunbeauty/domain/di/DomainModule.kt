package com.bunbeauty.domain.di

import com.bunbeauty.domain.util.cost.CostUtil
import com.bunbeauty.domain.util.cost.ICostUtil
import com.bunbeauty.domain.util.date_time.DateTimeUtil
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.domain.util.order.IOrderUtil
import com.bunbeauty.domain.util.order.OrderUtil
import com.bunbeauty.domain.util.product.IProductUtil
import com.bunbeauty.domain.util.product.ProductUtil
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    //UTIL

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
}