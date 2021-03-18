package com.bunbeauty.fooddeliveryadmin.di.modules

import com.bunbeauty.domain.repository.api.firebase.ApiRepository
import com.bunbeauty.common.utils.DataStoreHelper
import com.bunbeauty.common.utils.IDataStoreHelper
import com.bunbeauty.domain.repository.api.firebase.IApiRepository
import com.bunbeauty.domain.string_helper.IStringHelper
import com.bunbeauty.domain.string_helper.StringHelper
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class ApiModule {

    //NETWORK

    @Binds
    abstract fun bindApiRepository(apiRepository: ApiRepository): IApiRepository

    // DATA_STORE

    @Singleton
    @Binds
    abstract fun bindDataStoreHelper(dataStoreHelper: DataStoreHelper): IDataStoreHelper

    @Binds
    abstract fun bindStringHelper(stringHelper: StringHelper): IStringHelper

}