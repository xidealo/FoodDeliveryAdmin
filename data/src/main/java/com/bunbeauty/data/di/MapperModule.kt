package com.bunbeauty.data.di

import com.bunbeauty.data.mapper.cafe.EntityCafeMapper
import com.bunbeauty.data.mapper.cafe.IEntityCafeMapper
import com.bunbeauty.data.mapper.cafe.IServerCafeMapper
import com.bunbeauty.data.mapper.cafe.ServerCafeMapper
import com.bunbeauty.data.mapper.order.IServerOrderMapper
import com.bunbeauty.data.mapper.order.ServerOrderMapper
import com.bunbeauty.data.mapper.statistic.StatisticMapper
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MapperModule {

    @Singleton
    @Binds
    abstract fun bindEntityCafeMapper(entityCafeMapper: EntityCafeMapper): IEntityCafeMapper

    @Singleton
    @Binds
    abstract fun bindServerCafeMapper(serverCafeMapper: ServerCafeMapper): IServerCafeMapper

    @Singleton
    @Binds
    abstract fun bindServerOrderMapper(serverOrderMapper: ServerOrderMapper): IServerOrderMapper
}