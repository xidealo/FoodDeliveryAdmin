package com.bunbeauty.fooddeliveryadmin.di.modules

import com.bunbeauty.fooddeliveryadmin.data.api.firebase.ApiRepository
import com.bunbeauty.fooddeliveryadmin.data.api.firebase.IApiRepository
import com.bunbeauty.fooddeliveryadmin.data.local.storage.DataStoreHelper
import com.bunbeauty.fooddeliveryadmin.data.local.storage.IDataStoreHelper
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ApiModule {

    //NETWORK

    @Binds
    abstract fun bindApiRepository(apiRepository: ApiRepository): IApiRepository
/*
    @Binds
    abstract fun bindApiRepository(apiRepository: ApiRepository): IApiRepository
*/

    // DATA_STORE

    @Singleton
    @Binds
    abstract fun bindDataStoreHelper(dataStoreHelper: DataStoreHelper): IDataStoreHelper

}