package com.bunbeauty.data.di

import com.bunbeauty.data.mapper.MenuProductMapper
import com.bunbeauty.data.mapper.OderProductMapper
import com.bunbeauty.data.mapper.cafe.CafeMapper
import com.bunbeauty.data.mapper.category.CategoryMapper
import com.bunbeauty.data.mapper.city.CityMapper
import com.bunbeauty.data.mapper.nonworkingday.NonWorkingDayMapper
import com.bunbeauty.data.mapper.order.IServerOrderMapper
import com.bunbeauty.data.mapper.order.ServerOrderMapper
import com.bunbeauty.data.mapper.statistic.StatisticDayDetailMapper
import com.bunbeauty.data.mapper.statistic.StatisticMapper
import com.bunbeauty.data.repository.AdditionRepository
import org.koin.dsl.module

fun mapperModule() =
    module {
        single<IServerOrderMapper> {
            ServerOrderMapper(
                oderProductMapper = get(),
            )
        }

        single { CafeMapper() }

        single { NonWorkingDayMapper() }

        single { CategoryMapper() }

        single { MenuProductMapper() }

        single { OderProductMapper() }

        single { CityMapper() }

        single {
            StatisticMapper()
        }

        single {
            StatisticDayDetailMapper()
        }

        single {
            AdditionRepository(
                networkConnector = get(),
            )
        }
    }
