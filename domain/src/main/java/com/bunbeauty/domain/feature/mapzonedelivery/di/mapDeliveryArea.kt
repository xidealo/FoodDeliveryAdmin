package com.bunbeauty.domain.feature.mapzonedelivery.di

import com.bunbeauty.domain.feature.mapzonedelivery.GetDeliveryZoneUseCase
import com.bunbeauty.domain.feature.mapzonedelivery.editinfodeliveryzone.GetZoneUseCase
import com.bunbeauty.domain.feature.mapzonedelivery.editinfodeliveryzone.SaveInfoZoneUseCase
import org.koin.dsl.module

fun mapDeliveryArea() =
    module {
        factory {
            GetDeliveryZoneUseCase(
                dataStoreRepo = get(),
                cafeRepo = get(),
            )
        }

        factory {
            GetZoneUseCase(
                dataStoreRepo = get(),
                cafeRepo = get(),
            )
        }

        factory {
            SaveInfoZoneUseCase(
                dataStoreRepo = get(),
                cafeRepo = get(),
            )
        }
    }
