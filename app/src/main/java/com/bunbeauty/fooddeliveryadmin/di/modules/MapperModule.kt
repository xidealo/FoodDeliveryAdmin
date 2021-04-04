package com.bunbeauty.fooddeliveryadmin.di.modules

import com.bunbeauty.common.Mapper
import com.bunbeauty.data.model.MenuProduct
import com.bunbeauty.data.model.firebase.MenuProductFirebase
import com.bunbeauty.data.model.mapper.MenuProductMapper
import dagger.Binds
import dagger.Module

@Module
abstract class MapperModule {

    @Binds
    abstract fun bindsMovieDetailMapper(mapper: MenuProductMapper): Mapper<MenuProductFirebase, MenuProduct>
}