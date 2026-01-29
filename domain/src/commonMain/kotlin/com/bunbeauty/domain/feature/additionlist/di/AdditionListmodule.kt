package com.bunbeauty.domain.feature.additionlist.di

import com.bunbeauty.domain.feature.additionlist.CreateAdditionUseCase
import com.bunbeauty.domain.feature.additionlist.GetAdditionListNameUseCase
import com.bunbeauty.domain.feature.additionlist.GetSeparatedAdditionListUseCase
import com.bunbeauty.domain.feature.additionlist.SaveAdditionGroupForMenuProductListPriorityUseCase
import com.bunbeauty.domain.feature.additionlist.UpdateAdditionUseCase
import com.bunbeauty.domain.feature.additionlist.UpdateVisibleAdditionUseCase
import com.bunbeauty.domain.usecase.GetAdditionUseCase
import org.koin.dsl.module

fun additionModule() =
    module {
        factory {
            GetSeparatedAdditionListUseCase(
                dataStoreRepo = get(),
                additionRepo = get(),
                getNewUuidUseCase = get(),
            )
        }

        factory {
            UpdateVisibleAdditionUseCase(
                dataStoreRepo = get(),
                additionRepo = get(),
            )
        }

        factory {
            UpdateAdditionUseCase(
                dataStoreRepo = get(),
                additionRepo = get(),
                deletePhotoUseCase = get(),
                uploadPhotoUseCase = get(),
            )
        }
        factory {
            CreateAdditionUseCase(
                dataStoreRepo = get(),
                additionRepo = get(),
                uploadPhotoUseCase = get(),
            )
        }

        factory {
            GetAdditionUseCase(
                additionRepo = get(),
                dataStoreRepo = get(),
            )
        }
        factory {
            GetAdditionListNameUseCase()
        }

        factory {
            SaveAdditionGroupForMenuProductListPriorityUseCase(
                menuProductToAdditionGroupRepository = get(),
                dataStoreRepo = get(),
            )
        }
    }
