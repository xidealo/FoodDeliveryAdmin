package com.bunbeauty.fooddeliveryadmin.di.modules

import com.bunbeauty.data.mapper.Mapper
import com.bunbeauty.domain.model.MenuProduct
import com.bunbeauty.domain.model.firebase.MenuProductFirebase
import com.bunbeauty.data.mapper.MenuProductMapper
import dagger.Binds
import dagger.Module

@Module
abstract class MapperModule {

    @Binds
    abstract fun bindsMovieDetailMapper(mapper: MenuProductMapper): Mapper<MenuProductFirebase, MenuProduct>
}