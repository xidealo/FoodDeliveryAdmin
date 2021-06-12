package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.data.mapper.IMenuProductMapper
import com.bunbeauty.data.mapper.MenuProductMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {

    @Singleton
    @Binds
    abstract fun bindMenuProductMapper(menuProductMapper: MenuProductMapper): IMenuProductMapper

}