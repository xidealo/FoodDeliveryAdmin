package com.bunbeauty.domain.feature.editcafe.di

import com.bunbeauty.domain.feature.editcafe.CreateCafeNonWorkingDayUseCase
import com.bunbeauty.domain.feature.editcafe.DeleteCafeNonWorkingDayUseCase
import com.bunbeauty.domain.feature.editcafe.GetCafeWorkingHoursByUuidUseCase
import com.bunbeauty.domain.feature.editcafe.GetInitialNonWorkingDayDateUseCase
import com.bunbeauty.domain.feature.editcafe.GetMinNonWorkingDayDateUseCase
import com.bunbeauty.domain.feature.editcafe.GetNonWorkingDayListByCafeUuidUseCase
import com.bunbeauty.domain.feature.editcafe.UpdateCafeFromTimeUseCase
import com.bunbeauty.domain.feature.editcafe.UpdateCafeToTimeUseCase
import org.koin.dsl.module

fun editCafeModule() =
    module {
        factory {
            CreateCafeNonWorkingDayUseCase(
                dataStoreRepo = get(),
                cafeRepo = get(),
                nonWorkingDayRepo = get(),
            )
        }

        factory {
            DeleteCafeNonWorkingDayUseCase(
                dataStoreRepo = get(),
                nonWorkingDayRepo = get(),
            )
        }

        factory {
            GetCafeWorkingHoursByUuidUseCase(
                cafeRepo = get(),
                dateTimeUtil = get(),
            )
        }

        factory {
            GetInitialNonWorkingDayDateUseCase()
        }

        factory {
            GetMinNonWorkingDayDateUseCase()
        }

        factory {
            GetNonWorkingDayListByCafeUuidUseCase(
                cafeRepo = get(),
                dateTimeUtil = get(),
                nonWorkingDayRepo = get(),
                timeService = get(),
            )
        }

        factory {
            UpdateCafeFromTimeUseCase(
                cafeRepo = get(),
                dataStoreRepo = get(),
                dateTimeUtil = get(),
            )
        }

        factory {
            UpdateCafeToTimeUseCase(
                cafeRepo = get(),
                dataStoreRepo = get(),
                dateTimeUtil = get(),
            )
        }
    }
