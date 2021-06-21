package com.bunbeauty.fooddeliveryadmin.di.modules

import com.bunbeauty.data.repository.ApiRepository
import com.bunbeauty.data.repository.DataStoreRepository
import com.bunbeauty.domain.util.cost.CostUtil
import com.bunbeauty.domain.util.cost.ICostUtil
import com.bunbeauty.domain.util.date_time.DateTimeUtil
import com.bunbeauty.domain.util.date_time.IDateTimeUtil
import com.bunbeauty.domain.util.order.IOrderUtil
import com.bunbeauty.domain.util.order.OrderUtil
import com.bunbeauty.domain.util.product.IProductUtil
import com.bunbeauty.domain.util.product.ProductUtil
import com.bunbeauty.domain.repo.ApiRepo
import com.bunbeauty.domain.repo.DataStoreRepo
import com.bunbeauty.domain.util.resources.IResourcesProvider
import com.bunbeauty.domain.util.resources.ResourcesProvider
import com.bunbeauty.fooddeliveryadmin.utils.IStringUtil
import com.bunbeauty.fooddeliveryadmin.utils.StringUtil
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ApiModule {

    //NETWORK

    @Singleton
    @Binds
    abstract fun bindApiRepository(apiRepository: ApiRepository): ApiRepo

    // DATA_STORE

    @Singleton
    @Binds
    abstract fun bindDataStoreHelper(dataStoreHelper: DataStoreRepository): DataStoreRepo

    @Binds
    abstract fun bindStringHelperNew(stringUtil: StringUtil): IStringUtil

    @Binds
    abstract fun bindCostHelper(costHelper: CostUtil): ICostUtil

    @Binds
    abstract fun bindProductHelper(productHelper: ProductUtil): IProductUtil

    @Binds
    abstract fun bindResourcesProvider(resourcesProvider: ResourcesProvider): IResourcesProvider

    @Binds
    abstract fun bindDateTimeUtil(dateTimeUtil: DateTimeUtil): IDateTimeUtil

    @Binds
    abstract fun bindOrderUtil(orderUtil: OrderUtil): IOrderUtil

}