package com.bunbeauty.fooddeliveryadmin.di.modules

import com.bunbeauty.domain.repository.api.firebase.ApiRepository
import com.bunbeauty.common.utils.DataStoreHelper
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.domain.cost.CostUtil
import com.bunbeauty.domain.cost.ICostUtil
import com.bunbeauty.domain.date_time.DateTimeUtil
import com.bunbeauty.domain.date_time.IDateTimeUtil
import com.bunbeauty.domain.product.IProductUtil
import com.bunbeauty.domain.product.ProductUtil
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import com.bunbeauty.domain.resources.IResourcesProvider
import com.bunbeauty.domain.resources.ResourcesProvider
import com.bunbeauty.domain.string.IStringUtil
import com.bunbeauty.domain.string.StringUtil
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ApiModule {

    //NETWORK

    @Singleton
    @Binds
    abstract fun bindApiRepository(apiRepository: ApiRepository): IApiRepository

    // DATA_STORE

    @Singleton
    @Binds
    abstract fun bindDataStoreHelper(dataStoreHelper: DataStoreHelper): IDataStoreHelper

    @Binds
    abstract fun bindStringHelper(stringUtil: StringUtil): IStringUtil

    @Binds
    abstract fun bindCostHelper(costHelper: CostUtil): ICostUtil

    @Binds
    abstract fun bindProductHelper(productHelper: ProductUtil): IProductUtil

    @Binds
    abstract fun bindResourcesProvider(resourcesProvider: ResourcesProvider): IResourcesProvider

    @Binds
    abstract fun bindDateTimeUtil(dateTimeUtil: DateTimeUtil): IDateTimeUtil

}