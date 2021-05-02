package com.bunbeauty.fooddeliveryadmin.di.modules

import com.bunbeauty.domain.repository.api.firebase.ApiRepository
import com.bunbeauty.common.utils.DataStoreHelper
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.domain.cost_helper.CostHelper
import com.bunbeauty.domain.cost_helper.ICostHelper
import com.bunbeauty.domain.product.IProductHelper
import com.bunbeauty.domain.product.ProductHelper
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import com.bunbeauty.domain.resources.IResourcesProvider
import com.bunbeauty.domain.resources.ResourcesProvider
import com.bunbeauty.domain.string_helper.IStringHelper
import com.bunbeauty.domain.string_helper.StringHelper
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
    abstract fun bindStringHelper(stringHelper: StringHelper): IStringHelper

    @Binds
    abstract fun bindCostHelper(costHelper: CostHelper): ICostHelper

    @Binds
    abstract fun bindProductHelper(productHelper: ProductHelper): IProductHelper

    @Binds
    abstract fun bindResourcesProvider(resourcesProvider: ResourcesProvider): IResourcesProvider

}