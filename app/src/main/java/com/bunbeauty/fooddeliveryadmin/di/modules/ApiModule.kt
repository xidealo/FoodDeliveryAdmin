package com.bunbeauty.fooddeliveryadmin.di.modules

import com.bunbeauty.fooddeliveryadmin.data.api.firebase.ApiRepository
import com.bunbeauty.fooddeliveryadmin.data.api.firebase.IApiRepository
import dagger.Binds
import dagger.Module

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

/*    @Binds
    abstract fun bindDataStoreHelper(dataStoreHelper: DataStoreHelper): IDataStoreHelper
    */


}