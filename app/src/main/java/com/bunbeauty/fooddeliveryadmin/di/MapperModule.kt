package com.bunbeauty.fooddeliveryadmin.di

import com.bunbeauty.data.mapper.cafe.EntityCafeMapper
import com.bunbeauty.data.mapper.cafe.IEntityCafeMapper
import com.bunbeauty.data.mapper.cafe.IServerCafeMapper
import com.bunbeauty.data.mapper.cafe.ServerCafeMapper
import com.bunbeauty.data.mapper.cart_product.CartProductMapper
import com.bunbeauty.data.mapper.cart_product.ICartProductMapper
import com.bunbeauty.data.mapper.menu_product.IServerMenuProductMapper
import com.bunbeauty.data.mapper.menu_product.ServerMenuProductMapper
import com.bunbeauty.data.mapper.order.IServerOrderMapper
import com.bunbeauty.data.mapper.order.ServerOrderMapper
import com.bunbeauty.data.mapper.user_address.IUserAddressMapper
import com.bunbeauty.data.mapper.user_address.UserAddressMapper
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
    abstract fun bindCartProductMapper(cartProductMapper: CartProductMapper): ICartProductMapper

    @Singleton
    @Binds
    abstract fun bindServerMenuProductMapper(serverMenuProductMapper: ServerMenuProductMapper): IServerMenuProductMapper

    @Singleton
    @Binds
    abstract fun bindServerOrderMapper(serverOrderMapper: ServerOrderMapper): IServerOrderMapper

    @Singleton
    @Binds
    abstract fun bindUserAddressMapper(userAddressMapper: UserAddressMapper): IUserAddressMapper

}